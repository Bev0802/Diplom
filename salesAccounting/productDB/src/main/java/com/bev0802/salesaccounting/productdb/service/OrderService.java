package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.*;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import com.bev0802.salesaccounting.productdb.repository.OrderItemRepository;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import com.bev0802.salesaccounting.productdb.repository.ProductRepository;
import com.bev0802.salesaccounting.productdb.repository.specification.OrderSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервис для управления заказами в системе учета товаров.
 * Предоставляет функционал для создания, подтверждения и отмены заказов,
 * а также для получения информации о существующих заказах.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;



    /**
     * Создает новый заказ в системе.
     * Генерирует уникальный номер заказа на основе идентификаторов организаций и сотрудника.
     *
     * @param productId      Идентификатор продукта.
     * @param buyerOrganizationId Идентификатор организации покупателя.
     * @param employeeId     Идентификатор сотрудника.
     * @param quantity       Количество единиц продукта.
     * @param sellerOrganization Объект продавец
     *
     * @return Сохраненный заказ с присвоенным уникальным номером.
     */

    @Transactional
    public Order createOrder(Long buyerOrganizationId, Long employeeId, Long productId, BigDecimal quantity, Organization sellerOrganization) {
        Order order = new Order();

        // Получаем организацию и сотрудника по их ID, выбрасываем исключение, если не найдены
        Organization buyerOrganization = organizationService.findById(buyerOrganizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + buyerOrganizationId));
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Устанавливаем покупателя и сотрудника, создавшего заказ
        order.setBuyerOrganization(buyerOrganization);
        order.setSellerOrganization(sellerOrganization);
        order.setEmployee(employee);
        order.setStatus(OrderStatus.NEW);  // Установка статуса нового заказа
        order.setOrderDate(LocalDateTime.now()); // Установка текущей даты
        order.setStatusChangeDate(LocalDateTime.now());

        // Генерация уникального номера заказа
        String orderNumber = generateOrderNumber(buyerOrganizationId, sellerOrganization.getId());
        order.setOrderNumber(orderNumber);

        // Сохранение заказа в базе данных для получения ID
        order = orderRepository.save(order);

        // Создание нового элемента заказа
        OrderItem orderItem = new OrderItem();
        Product product = productService.getProductById(productId);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        BigDecimal itemTotal = product.getPrice().multiply(quantity);
        orderItem.setAmount(itemTotal);

        // Устанавливаем заказ для OrderItem
        long orderId = order.getId();
        orderItem.setOrderId(orderId);

        // Добавление нового элемента в заказ
        Set<OrderItem> items = new HashSet<>();
        items.add(orderItem);
        order.setItems(items);

        // Расчет общей стоимости заказа
        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        // Обновление заказа с элементами
        order = orderRepository.save(order);

        logger.info("ЗАКАЗ после СОХРАНЕНИЯ: {}", order);
        logger.info("ТОВАР после СОХРАНЕНИЯ: {}", order.getItems());
        for (OrderItem item : order.getItems()) {
            logger.info("Товар: {}, Количество: {}, Цена: {}, Сумма: {}",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getAmount());
        }
        return order;
    }

    /**
     * Генерирует уникальный номер заказа.
     * Формат номера: "s{buyerId}_b{sellerId}/{sequenceNumber}".
     *
     * @return Строка, представляющая уникальный номер заказа.
     */
    private String generateOrderNumber(Long buyerOrganizationId, Long sellerOrganizationId) {
        String prefix = "b" +  buyerOrganizationId + "_s" + sellerOrganizationId;
        int nextNumber = orderRepository.findNextNumberForPrefix(prefix);
        return prefix + "/" + nextNumber;
    }

    /**
     * Добавляет продукт в существующий заказ.
     * @param order
     * @param product
     * @param quantity
     * @return
     */
    @Transactional
    public Order addItemToOrder(Order order, Product product, BigDecimal quantity) {
        // Получаем список элементов заказа
        Set<OrderItem> items = order.getItems();
        // Сохраняем текущую общую сумму заказа
        BigDecimal previousTotalAmount = order.getTotalAmount();

        // Создание нового элемента заказа
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        BigDecimal itemTotal = product.getPrice().multiply(quantity);
        orderItem.setAmount(itemTotal);
        // Привязываем элемент заказа к заказу
        orderItem.setOrderId(order.getId()); // Установка orderId

        // Добавляем элемент в заказ и пересчитываем общую стоимость
        items.add(orderItem);

        // Пересчитываем общую стоимость заказа, добавляя к предыдущей сумме новую сумму товара
        BigDecimal totalAmount = previousTotalAmount.add(itemTotal);
        order.setTotalAmount(totalAmount);

        // Сохранение заказа в базе данных
        orderRepository.save(order);

        return order;
    }


    /**
     * Находит заказ по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @return Объект заказа.
     * @throws RuntimeException Если заказ не найден.
     */
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Возвращает список всех заказов в системе.
     *
     * @return Список всех заказов.
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * Возвращает список заказов, размещенных организацией-покупателем.
     *
     * @param buyer_organization_id Идентификатор организации-покупателя.
     * @return Список заказов, размещенных организацией.
     */
    public List<Order> findOrdersByBuyerId(Long buyer_organization_id) {
        return orderRepository.findByBuyerOrganizationId(buyer_organization_id);
    }

    /**
     * Возвращает список всех заказов, полученных организацией-продавцом, исключая заказы со статусом NEW.
     *
     * @param seller_organization_id Идентификатор организации-продавца.
     * @return Список заказов, полученных организацией, за исключением тех, что имеют статус NEW.
     */
    public List<Order> findOrdersBySellerIdExcludingNew(Long seller_organization_id) {
        return orderRepository.findBySellerOrganizationIdAndStatusNot(seller_organization_id, OrderStatus.NEW);
    }

    //todo: разместить на форме как всплывающее уведомление о вновь поступившем заказе,
    // если не получится удалить.
    /**
     * Возвращает список подтвержденных заказов, полученных организацией-продавцом.
     * Можно использовать как отдельный список вновь полученных заказов.
     *
     * @param sellerId Идентификатор организации-продавца.
     * @return Список подтвержденных заказов, полученных организацией.
     */
    public List<Order> findConfirmedOrdersBySellerId(Long sellerId) {
        return orderRepository.findBySellerOrganizationIdAndStatus(sellerId, OrderStatus.CONFIRMED);
    }

    /**
     * Получает список всех заказов, созданных конкретным сотрудником.
     *
     * @param employeeId Идентификатор сотрудника.
     * @return Список заказов, созданных сотрудником.
     */
    public List<Order> findOrdersByEmployeeId(Long employeeId) {
        return orderRepository.findByEmployeeId(employeeId);
    }

    //#region Изменение статусов заказов
    /**
     * Подтверждает заказ по его идентификатору.
     * Переводит статус заказа в "CONFIRMED" и резервирует товары.
     *
     * @param orderId Идентификатор заказа для подтверждения.
     * @return Обновленный заказ.
     * @throws RuntimeException Если заказ не найден или уже подтвержден.
     */
    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(OrderStatus.CONFIRMED);
            order.setStatusChangeDate(LocalDateTime.now()); // Обновление времени изменения статуса
            //todo: реализовать резервирование товара
            order.getItems().forEach(item -> {
                int quantity = item.getQuantity().intValueExact(); // Безопасное преобразование, бросает ArithmeticException, если есть дробная часть.
                productService.reserveProduct(item.getProduct().getId(), quantity);
                logger.info("Product: " + item.getProduct());
            });
        }
        return orderRepository.save(order);
    }
    /**
     * Меняет статус заказа на 'PAID' и сохраняет изменения в базе данных.
     *
     * @param orderId Идентификатор заказа, который нужно оплатить.
     * @return Обновленный заказ после оплаты.
     */
    @Transactional
    public Order payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.NEW) {
            order.setStatus(OrderStatus.PAID);
            order.setStatusChangeDate(LocalDateTime.now());
        } else {
            throw new IllegalStateException("Заказ должен быть заказ уже оплачен");
        }
        //todo: реализовать оплату
        //todo: реализовать создание документа оплаты
        //todo: реализовать резервирование товара
        return orderRepository.save(order);
    }

    /**
     * Отгружает заказ по его идентификатору и создает новый документ реализации товаров
     * в который переносятся все данные из заказа, а затем уменьшает количество резервированных товаров
     * @param orderId идентификатор
     * @return
     */
    @Transactional
        public Order shipOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.SHIPPED);
            order.setStatusChangeDate(LocalDateTime.now());
        }else {
            throw new IllegalStateException("Заказ должен быть ОПЛАТЕН, прежде чем его можно будет ОТПРАВИТЬ");
        }
        return orderRepository.save(order);
    }

    /**
     * Отменяет заказ по его идентификатору.
     * Переводит статус заказа в "CANCELLED" и возвращает резервированные товары на склад.
     *
     * @param orderId Идентификатор заказа для отмены.
     * @return Обновленный заказ.
     * @throws RuntimeException Если заказ не найден или не находится в статусе "CONFIRMED".
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setStatusChangeDate(LocalDateTime.now()); // Обновление времени изменения статуса
            order.getItems().forEach(item -> {
                int quantity = item.getQuantity().intValueExact(); // Безопасное преобразование
                productService.returnProduct(item.getProduct().getId(), quantity);
            });
        }else{
            throw new RuntimeException("Order must be SHIPPED before it can be CANCELLED");
        }
        return orderRepository.save(order);
    }
    //#endregion



    /**
     * Проверяет, принадлежит ли продукт другой организации, а не той, которая пытается сделать заказ.
     *
     * @param productId Идентификатор продукта.
     * @param organizationId Идентификатор организации, которая пытается сделать заказ.
     * @return true, если продукт принадлежит другой организации, иначе false.
     */
    public boolean isProductFromAnotherOrganization(Long productId, Long organizationId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return !product.getOrganization().getId().equals(organizationId);
    }

    /**
     * Получает отфильтрованный список заказов на основе заданных критериев.
     *
     * @param sellerId Идентификатор продавца.
     * @param buyerOrganizationId Идентификатор покупателя.
     * @param status Статус заказа.
     * @param startDate Начальная дата периода.
     * @param endDate Конечная дата периода.
     * @return Список отфильтрованных заказов.
     */
    public List<Order> getFilteredOrders(Long sellerId,
                                         Long buyerOrganizationId,
                                         OrderStatus status, LocalDateTime startDate,
                                         LocalDateTime endDate) {
        Specification<Order> spec = OrderSpecification.filterByCriteria(sellerId, buyerOrganizationId, status, startDate, endDate);
        return orderRepository.findAll(spec, Sort.by("orderDate").descending());
    }

    /**
     * Удаляет продукт из заказа и сохраняет изменения в базе данных.
     *
     * @param orderId Заказ, из которого нужно удалить продукт.
     * @param orderItemId Идентификатор продукта, который нужно удалить.
     * @return Обновленный заказ после удаления продукта.
     */
    @Transactional
    public void removeProductFromOrder(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        order.getItems().remove(item);
        orderItemRepository.delete(item); // Удаление из репозитория элемента заказа
        orderRepository.save(order);
    }

    @Transactional
    public void updateProductQuantityInOrder(Long orderId, Long orderItemId, BigDecimal newQuantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // Проверяем, что новое количество не превышает доступное количество на складе
        if (item.getProduct().getQuantity().compareTo(newQuantity) < 0) {
            throw new RuntimeException("Insufficient product stock");
        }

        item.setQuantity(newQuantity);
        orderItemRepository.save(item); // Сохраняем измененное количество
    }

    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        List<OrderItem> items = orderRepository.findItemsByOrderId(orderId);
        logger.info("OrderItems for Order ID {}: {}", orderId, items);
        return items;
    }

    public List<Order> findNewOrdersByBuyerId(Long buyerOrganizationId) {
        return orderRepository.findByBuyerOrganizationIdAndStatus(buyerOrganizationId, OrderStatus.NEW);

    }
    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}




