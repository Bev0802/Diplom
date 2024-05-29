package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.model.Document;
import com.bev0802.salesaccounting.productdb.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization/{organizationId}/employee/{employeeID}/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    /**
     * Создает документ на основе заказа.
     * @param orderId Идентификатор заказа, на основе которого будет создан документ.
     * @return ResponseEntity с созданным документом.
     */
    @PostMapping("/createFromOrder/{orderId}")
    public ResponseEntity<Document> createDocumentFromOrder(@PathVariable("orderId") Long orderId) {
        try {
            Document document = documentService.createDocumentFromOrder(orderId);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // В идеале следует возвращать более информативное сообщение об ошибке.
        }
    }
}
