package com.bev0802.salesaccounting.productdb.controller;


import com.bev0802.salesaccounting.productdb.model.Document;
import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.model.OrderItem;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import com.bev0802.salesaccounting.productdb.service.DocumentService;
import com.bev0802.salesaccounting.productdb.service.OrderService;
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

    @Autowired
    private OrderService orderService;
    @Autowired
    private DocumentService documentService;

    /**
     * Получение списка всех заказов системы.
     * @return Список всех заказов.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

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
     * Получение заказа по идентификатору
     *
     * @param order_id
     * @return Заказ
     */

    @GetMapping("/{order_id}")
    public Order getOrderById(@PathVariable("order_id") Long order_id) {
        return ResponseEntity.ok(orderService.findById(order_id)).getBody();
    }

    /**
     * Создает новый заказ для организации и для сотрудника которой авторизивался.
     * @param order Данные заказ(покупку)
     * @param buyer_id ID организации
     * @param employee_id ID сотрудника
     * @return Созданный заказ.
     */
    @PostMapping("/newOrder")
    public ResponseEntity<Order> createOrder(@RequestBody Order order,
                                             @PathVariable("organizationId") Long buyer_id,
                                             @PathVariable("employeeId") Long employee_id) {
        return ResponseEntity.ok(orderService.createOrder(order, buyer_id, employee_id));
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


    /**
     * Добавляет продукт в существующий заказ.
     *
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param orderId Идентификатор заказа, в который добавляется продукт.
     * @param productId Идентификатор продукта для добавления.
     * @param quantity Количество добавляемого продукта.
     * @return ResponseEntity с обновленным заказом или ошибкой.
     */
    @PostMapping("/addProduct/{order_id}")
    public ResponseEntity<?> addProductToOrder(
            @PathVariable("organizationId") Long organizationId,
            @PathVariable("employeeId") Long employeeId,
            @PathVariable("order_id") Long orderId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity) {
        try {
            orderService.addProductToOrder(orderId, productId, quantity, organizationId, employeeId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
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
