package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.model.Document;
import com.bev0802.salesaccounting.productdb.model.DocumentItem;
import com.bev0802.salesaccounting.productdb.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    /**
     * Находит документ по идентификатору
     * @param documentId
     * @return
     */
    @GetMapping("/{documentId}")
    public ResponseEntity <Document>getDocumentById(@PathVariable("documentId") Long documentId) {
        Document document = documentService.findDocumentById(documentId);
        return ResponseEntity.ok(document);
    }

    /**
     * Получение списка документов, по идентификатору организации продавца
     * @param sellerOrganizationId
     */
    @GetMapping("/sellerList")
    public ResponseEntity <List<Document>>getDocumentsBySeller(@PathVariable("organizationId") Long sellerOrganizationId) {
        List<Document> documents = documentService.findOrdersBySellerId(sellerOrganizationId);
        return ResponseEntity.ok(documents);
    }
    /**
     * Получение списка документов, по идентификатору организации покупателя
     * @param buyerOrganizationId
     * @return
     */
    @GetMapping("/buyerList")
    public ResponseEntity <List<Document>>getDocumentsByBuyer(@PathVariable("organizationId") Long buyerOrganizationId) {
        List<Document> documents = documentService.findOrdersByBuyerId(buyerOrganizationId);
        return ResponseEntity.ok(documents);
    }
    /**
     * Получение списка товаров, по идентификатору документа
     * @param documentId
     * @return Список товаров в документе
     */
    @GetMapping("/productList/{documentId}")
    public ResponseEntity <List<DocumentItem>>findItemsByDocumentId(@PathVariable("documentId") Long documentId) {
        List<DocumentItem> items = documentService.findItemsByDocumentId(documentId);
        return ResponseEntity.ok(items);
    }

    /**
     * Обновляет документ
     * @param documentId
     * @return
     */
    @PutMapping("/update/{documentId}")
    public ResponseEntity <Document>updateDocument(@PathVariable("documentId") Long documentId) {
        Document document = documentService.updateDocument(documentId);
        return ResponseEntity.ok(document);
    }
    /**
     * Удаляет документ
     * @param documentId
     * @return
     */
    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<Void>deleteDocument(@PathVariable("documentId") Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok().build();
    }


}
