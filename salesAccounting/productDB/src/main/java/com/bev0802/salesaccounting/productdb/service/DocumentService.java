package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Document;
import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.repository.DocumentRepository;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProductService productService;
    @Transactional
    public Document createDocumentFromOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Создание и заполнение документа данными из заказа
        Document document = new Document();

        document.setBuyerOrganization(order.getBuyerOrganization());
        document.setSellerOrganization(order.getSellerOrganization());

        // Прочие поля документа

        documentRepository.save(document);
        return document;
    }

}
