package com.bev0802.salesaccounting.productdb.controller;


import com.bev0802.salesaccounting.productdb.model.*;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import com.bev0802.salesaccounting.productdb.service.DocumentService;
import com.bev0802.salesaccounting.productdb.service.OrderService;
import com.bev0802.salesaccounting.productdb.service.OrganizationService;
import com.bev0802.salesaccounting.productdb.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organization/{organizationId}/employee/{employeeId}/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private DocumentService documentService;
    @Autowired ProductService productService;
    @Autowired
    private OrganizationService organizationService;


    /**
     * Создает новый заказ для организации и для сотрудника которой авторизивался.
     * @param order Данные заказ(покупку)
     * @param buyerOrganizationId ID организации
     * @param employeeId ID сотрудника
     * @return Созданный заказ.
     */
    @PostMapping("/newOrder/{productId}/{quantity}")
    public ResponseEntity<Order> createOrder(@RequestBody Order order,
                                             @PathVariable("organizationId") Long buyerOrganizationId,
                                             @PathVariable("employeeId") Long employeeId,
                                             @PathVariable("productId") Long productId,
                                             @PathVariable("quantity") BigDecimal quantity) {
        // Получение продукта и организации продавца
        Product product = productService.getProductById(productId);
        Organization sellerOrganization = product.getOrganization();

        return ResponseEntity.ok(orderService.createOrder(buyerOrganizationId, employeeId, productId, quantity, sellerOrganization));
    }
    /**
     * Добавляет продукт в заказ, если заказа нет создает новый и добавляет туда товар,
     * а если заказ уже есть добавляет в существующий при этом отбирает заказ по продавцу и владельцу товара.
     *
     * @param buyerOrganizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param productId Идентификатор продукта для добавления.
     * @param quantity Количество добавляемого продукта.
     * @return ResponseEntity с обновленным заказом или ошибкой.
     */

    @PostMapping("/addProductToOrder/{productId}/{quantity}")
    public ResponseEntity<?> addProductToOrder(
            @PathVariable("organizationId") Long buyerOrganizationId,
            @PathVariable("employeeId") Long employeeId,
            @PathVariable Long productId,
            @PathVariable BigDecimal quantity) {
        try {
            // Логирование полученных данных
            logger.info("Получен запрос на добавление товара в заказ. Идентификатор организации покупателя: {}, " +
                            "Идентификатор сотрудника: {}, Идентификатор продукта: {}, Количество: {}",
                    buyerOrganizationId, employeeId, productId, quantity);

            // Получение продукта и организации продавца
            Product product = productService.getProductById(productId);
            Organization sellerOrganization = product.getOrganization();

            // Поиск существующих заказов
            List<Order> orders = orderService.findNewOrdersByBuyerId(buyerOrganizationId);
            Order order = orders.stream()
                    .filter(o -> o.getSellerOrganization().getId().equals(sellerOrganization.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        logger.info("Существующих заказов от того же продавца не найдено. Создание нового заказа.");
                        return orderService.createOrder(buyerOrganizationId, employeeId, productId, quantity, sellerOrganization);
                    });

            if (!orders.contains(order)) {
                // Новый заказ был создан, товар уже добавлен в него в createOrder методе
                logger.info("Товар уже добавлен в новый заказ.");
            } else {
                // Добавляем товар в существующий заказ
                logger.info("Добавление товара в существующий заказ.");
                orderService.addItemToOrder(order, product, quantity);
            }

            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            // Логирование исключения
            logger.error("Произошла ошибка при обработке запроса: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    //#region получение списков
    /**
     * Получение списка всех заказов системы.
     * @return Список всех заказов.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    //#region Получение заказов и данных их них
    /**
     * Получение списка заказов(покупок) по ID органиазции которая вошла.
     * @param buyerOrganizationId покупателя.
     * @return Список заказов покупателя.
     */
    @GetMapping("/buyerList")
    public ResponseEntity<List<Order>> getOrdersByBuyer(@PathVariable("organizationId") Long buyerOrganizationId,
                                                        @PathVariable("employeeId") Long employeeId) {
        List<Order> orders = orderService.findOrdersByBuyerId(buyerOrganizationId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Получение списка заказов по ID продавца, исключая новые.
     * @param sellerOrganizationId ID продавца.
     * @return Список заказов.
     * */
    @GetMapping("/sellerList")
    public ResponseEntity<List<Order>> getOrdersBySeller(@PathVariable("organizationId") Long sellerOrganizationId,
                                                         @PathVariable("employeeId") Long employeeId) {
        List<Order> orders = orderService.findOrdersBySellerIdExcludingNew(sellerOrganizationId);
        return ResponseEntity.ok(orders);
    }
    /**
     * Получение списка новых заказов по ID покупателя.
     * @param buyerOrganizationId ID покупателя.
     * @return Список новых заказов.
     */

    @GetMapping("/newList")
    public ResponseEntity<List<Order>> getNewOrdersByBuyer(@PathVariable("organizationId") Long buyerOrganizationId,
                                                           @PathVariable("employeeId") Long employeeId) {
        List<Order> orders = orderService.findNewOrdersByBuyerId(buyerOrganizationId);
        return ResponseEntity.ok(orders);
    }

  //#endregion

    /**
     * Получение заказа по идентификатору
     *
     * @param order_id
     * @return Заказ
     */

    @GetMapping("/{order_id}")
    public Order getOrderById(@PathVariable("order_id") Long order_id) {
        return ResponseEntity.ok(orderService.findById(order_id)).getBody();
    }


    //todo: добавить в уведомление о новом заказе в web приложении.
    /**
     * Получение списка подтвержденных заказов по ID продавца.
     * @param seller_id ID продавца.
     * @return Список подтвержденных заказов.
     */
    @GetMapping("/seller/confirmed")
    public ResponseEntity<List<Order>> getConfirmedOrdersBySeller(@PathVariable("organizationId") Long seller_id) {
        List<Order> orders = orderService.findConfirmedOrdersBySellerId(seller_id);
        return ResponseEntity.ok(orders);
    }
    //#endregion

    /**
     * Получение списка товарных позиций по идентификатору заказа.
     * @param orderId ID заказа.
     * @return Список товарных позиций в заказе.
     */
    @GetMapping("/{order_id}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable("order_id") Long orderId) {
        List<OrderItem> items = orderService.findOrderItemsByOrderId(orderId);
        if (items != null) {
            return ResponseEntity.ok(items);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //#region Изменение статусов заказов
    /**
     * Подтверждение заказа по его ID. Меняет статус NEW на CONFIRMED. И резервирует товар.
     * @param orderId ID заказа.
     * @return Обновленный заказ.
     */
    @PostMapping("/confirm/{order_id}")
    public ResponseEntity<Order> confirmOrder(@PathVariable("order_id") Long orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    /**
     * Оплата заказа по его ID. Меняет статус CONFIRMED на PAID. И оплачивает заказ.
     */
    @PostMapping("/pay/{order_id}")
    public ResponseEntity<Order> payOrder(@PathVariable("order_id") Long orderId) {
        return ResponseEntity.ok(orderService.payOrder(orderId));
    }

    /**
     * Отгрузка заказа по его ID. Меняет статус PAID на SHIPPED.
     * @param orderId
     * @return
     */
    @PostMapping("/ship/{order_id}")
    public ResponseEntity<?> shipOrderAndCreateDocument(@PathVariable("order_id") Long orderId) {
        Order shippedOrder = orderService.shipOrder(orderId);
        Document document = documentService.createDocumentFromOrder(orderId);
        return ResponseEntity.ok(Map.of("order", shippedOrder, "document", document));
    }

    /**
     * Отмена заказа по его ID.
     * @param orderId ID заказа.
     * @return Обновленный заказ.
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
//#endregion
    //todo: в фильтре работает только отбр по sellerId, все остальное не работает.
    /**
     * Получение списка заказов по фильтрам.
     * @param sellerId Продавец.
     * @param buyerId Покупатель.
     * @param status Статус заказа.
     * @param startDate Начальная дата.
     * @return Отфильтрованный список заказов.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Order>> getFilteredOrders(
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<Order> orders = orderService.getFilteredOrders(sellerId, buyerId, status, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Получает список всех заказов, созданных конкретным сотрудником.
     * Этот метод позволяет клиентам API получать данные о всех заказах, созданных сотрудником.
     * @param organizationId ID организации, к которой принадлежит сотрудник.
     * @param employee_id Идентификатор сотрудника, создавшего заказы.
     * @return ResponseEntity, содержащий список заказов или ошибку, если таковая произойдет.
     */
    @GetMapping("/employeeOrders")
    public ResponseEntity<List<Order>> getOrdersByEmployee(@PathVariable Long organizationId, @PathVariable Long employee_id) {
        try {
            List<Order> orders = orderService.findOrdersByEmployeeId(employee_id);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            // Здесь можно обработать исключения, например, если сотрудник не найден
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаление товара из заказа
     * @param orderId
     * @param orderItemId
     * @return
     */
    @PostMapping("/removeProduct/{orderId}/{orderItemId}")
    public ResponseEntity<?> removeProductFromOrder(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderItemId") Long orderItemId) {
        try {
            orderService.removeProductFromOrder(orderId, orderItemId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Изменение количества продукта в заказе
     * @param orderId
     * @param orderItemId
     * @param quantity
     * @return
     */

    @PostMapping("/updateQuantity/{orderId}/{orderItemId}")
    public ResponseEntity<?> updateProductQuantityInOrder(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderItemId") Long orderItemId,
            @RequestParam("quantity") BigDecimal quantity) {
        try {
            orderService.updateProductQuantityInOrder(orderId, orderItemId, quantity);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
