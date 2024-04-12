package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        // Добавьте логику создания заказа
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        // Найдите заказ по id
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> findAll() {
        // Верните все заказы
        return orderRepository.findAll();
    }

    // Другие методы управления заказами
}
