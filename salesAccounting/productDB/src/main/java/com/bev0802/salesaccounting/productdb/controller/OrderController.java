package com.bev0802.salesaccounting.productdb.controller;


import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // Другие методы управления заказами
}

