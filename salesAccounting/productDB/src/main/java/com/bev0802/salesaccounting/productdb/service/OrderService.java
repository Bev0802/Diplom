package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.*;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import com.bev0802.salesaccounting.productdb.repository.OrderItemRepository;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import com.bev0802.salesaccounting.productdb.repository.ProductRepository;
import com.bev0802.salesaccounting.productdb.repository.specification.OrderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
/**
 * Сервис для управления заказами в системе учета товаров.
 * Предоставляет функционал для создания, подтверждения и отмены заказов,
 * а также для получения информации о существующих заказах.
 */
@Service
public class OrderService {

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
     * @param order Объект заказа для сохранения.
     * @param organizationId Идентификатор организации покупателя.
     * @param employeeId Идентификатор сотрудника.
     * @return Сохраненный заказ с присвоенным уникальным номером.
     */
    @Transactional
    public Order createOrder(Order order, Long organizationId, Long employeeId) {
        // Получаем организацию и сотрудника по их ID, выбрасываем исключение, если не найдены
        Organization buyerOrganization = organizationService.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + organizationId));
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Устанавливаем покупателя и сотрудника, создавшего заказ
        order.setBuyerOrganization(buyerOrganization);
        order.setEmployee(employee);
        order.setStatus(OrderStatus.NEW);  // Установка статуса нового заказа
        order.setOrderDate(LocalDateTime.now()); // Установка текущей даты

        // Генерация уникального номера заказа
        String orderNumber = generateOrderNumber(order);
        order.setOrderNumber(orderNumber);

        // Расчет и установка общей стоимости заказа
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product product = productService.getProductById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + item.getProductId());
            }
            item.setPrice(product.getPrice());  // Установка цены из продукта
            BigDecimal itemTotal = item.getPrice().multiply(item.getQuantity());
            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);

        // Сохранение заказа в базе данных
        return orderRepository.save(order);
    }

    /**
     * Генерирует уникальный номер заказа.
     * Формат номера: "ORD_{buyerId}_{sellerId}_{employeeId}/{sequenceNumber}".
     *
     * @param order Объект заказа, для которого необходимо сгенерировать номер.
     * @return Строка, представляющая уникальный номер заказа.
     */
    private String generateOrderNumber(Order order) {
        String prefix = "ORD_" + order.getBuyerOrganization().getId() + "_"
                + order.getSellerOrganization().getId() + "_"
                + order.getEmployee().getId();

        int nextNumber = orderRepository.findNextNumberForPrefix(prefix);

        return prefix + "/" + nextNumber;
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
     * @param buyerId Идентификатор организации-покупателя.
     * @return Список заказов, размещенных организацией.
     */
    public List<Order> findOrdersByBuyerId(Long buyerId) {
        return orderRepository.findByBuyerOrganizationId(buyerId);
    }

    /**
     * Возвращает список всех заказов, полученных организацией-продавцом, исключая заказы со статусом NEW.
     *
     * @param sellerId Идентификатор организации-продавца.
     * @return Список заказов, полученных организацией, за исключением тех, что имеют статус NEW.
     */
    public List<Order> findOrdersBySellerIdExcludingNew(Long sellerId) {
        return orderRepository.findBySellerOrganizationIdAndStatusNot(sellerId, OrderStatus.NEW);
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

            order.getItems().forEach(item -> {
                int quantity = item.getQuantity().intValueExact(); // Безопасное преобразование, бросает ArithmeticException, если есть дробная часть.
                productService.reserveProduct(item.getProduct().getId(), quantity);
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

        if (order.getStatus() != OrderStatus.PAID) {
            order.setStatus(OrderStatus.PAID);
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Order is already paid");
        }
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

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be PAID before it can be SHIPPED");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setStatusChangeDate(LocalDateTime.now());
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

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setStatusChangeDate(LocalDateTime.now()); // Обновление времени изменения статуса
            order.getItems().forEach(item -> {
                int quantity = item.getQuantity().intValueExact(); // Безопасное преобразование
                productService.returnProduct(item.getProduct().getId(), quantity);
            });
        }
        return orderRepository.save(order);
    }
    /**
     * Добавляет продукт в заказ.
     *
     * @param productId Идентификатор продукта, который нужно добавить.
     * @param id
     * @param quantity  Количество продукта, которое нужно добавить.
     * @return Обновленный заказ.
     */
    @Transactional
    public Order addProductToOrder(Long productId, Long id, BigDecimal quantity, Long organizationId, Long employeeId) {
        Product product = productService.getProductById(productId);

        // Проверяем, есть ли активный заказ для организации покупателя с продавцом, являющимся организацией продукта
        List<Order> activeOrders = orderRepository.findByStatusAndBuyerOrganizationIdOrSellerOrganizationId(
                OrderStatus.NEW, organizationId, product.getOrganization().getId());

        // Фильтруем заказы, где продавец совпадает с организацией продукта
        Order activeOrder = activeOrders.stream()
                .filter(order -> order.getSellerOrganization().getId().equals(product.getOrganization().getId()))
                .findFirst()
                .orElse(null);

        if (activeOrder != null) {
            // Если существует активный заказ с продавцом, равным организации продукта
            return addItemToExistingOrder(activeOrder, product, quantity);
        } else {
            // Создаём новый заказ, если активный заказ отсутствует или продавцы не совпадают
            return createNewOrderWithProduct(product, quantity, organizationId, employeeId);
        }
    }

    private Order addItemToExistingOrder(Order order, Product product, BigDecimal quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice()); // Установите цену на момент покупки

        // Добавляем товарную позицию в заказ
        order.getItems().add(orderItem);

        // Сохраняем изменения в заказе
        return orderRepository.save(order);
    }

    private Order createNewOrderWithProduct(Product product, BigDecimal quantity, Long organizationId, Long employeeId) {
        Order order = new Order();

        // Используйте orElseThrow для получения объекта Organization и Employee из Optional
        Organization buyerOrganization = organizationService.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + organizationId));
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        order.setBuyerOrganization(buyerOrganization);
        order.setSellerOrganization(product.getOrganization());
        order.setEmployee(employee);
        order.setStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDateTime.now());

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());

        // Создание и добавление OrderItem в заказ
        if (order.getItems() == null) {
            order.setItems(new HashSet<>());
        }
        order.getItems().add(orderItem);

        // Сохраняем новый заказ
        return orderRepository.save(order);
    }


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
    public List<Order> getFilteredOrders(Long sellerId, Long buyerOrganizationId, OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Order> spec = OrderSpecification.filterByCriteria(sellerId, buyerOrganizationId, status, startDate, endDate);
        return orderRepository.findAll(spec, Sort.by("orderDate").descending());
    }

    /**
     *
     * @param orderId
     * @param orderItemId
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
        // Пример вызова метода репозитория, который нужно реализовать
        return orderRepository.findItemsByOrderId(orderId);
    }
}




