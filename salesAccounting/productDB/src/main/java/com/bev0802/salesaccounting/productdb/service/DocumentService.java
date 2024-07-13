package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.*;
import com.bev0802.salesaccounting.productdb.repository.DocumentItemRepository;
import com.bev0802.salesaccounting.productdb.repository.DocumentRepository;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProductService productService;
    @Autowired
    DocumentItemRepository documentItemRepository;


    /**
     * Метод создает документ из заказа по идентификатору заказа
     * @param orderId идентификатор заказа
     * @return созданный документ
     */
    @Transactional
    public Document createDocumentFromOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Создание и заполнение документа данными из заказа
        Document document = new Document();
        document.setOrderId(orderId);
        document.setBuyerOrganization(order.getBuyerOrganization());
        document.setSellerOrganization(order.getSellerOrganization());
        document.setResponsibleEmployeeBuyer(order.getEmployee());
        document.setDocumentNumber(generateSellerDocumentNumber(order.getSellerOrganization()));
        document.setDocumentDate(LocalDateTime.now());
        document.setTotalAmount(order.getTotalAmount());
        // Сохранение документа
        documentRepository.save(document);
        logger.info("Документ создан: id {}, покупатель {}, продавец {}, номер {}, дата документа {}",
                document.getId(), document.getBuyerOrganization(), document.getSellerOrganization(),
                document.getDocumentNumber(), document.getDocumentDate());

        // todo: перенос данных из OrderItem в DocumentItem
        // Получение списка товаров из заказа
        List<OrderItem> orderItems = new ArrayList<>(order.getItems());
// Создание списка документов
        List<DocumentItem> documentItems = new ArrayList<>();

// Перенос данных из OrderItem в DocumentItem
        for (OrderItem orderItem : orderItems) {
            DocumentItem documentItem = new DocumentItem();
            documentItem.setDocumentId(document.getId());
            documentItem.setProduct(orderItem.getProduct());
            documentItem.setQuantity(orderItem.getQuantity());
            documentItem.setPrice(orderItem.getPrice());
            documentItem.setAmount(orderItem.getAmount());
            documentItemRepository.save(documentItem);
            documentItems.add(documentItem);
            logger.info("Товар добавлен в документ: {}", documentItem);
        }
        document.setItems(documentItems);
        documentRepository.save(document);


        logger.info("Документ создан: id {}, покупатель {}, продавец {}, номер {}, дата документа {}, товары: {}",
                document.getId(), document.getBuyerOrganization(), document.getSellerOrganization(),
                document.getDocumentNumber(), document.getDocumentDate(), document.getItems());

        return document;
    }

    /**
     * Генерация номера документа для продавца
     * @param sellerOrganization оргоаниация продавец
     * @return генерированный номер
     */
    public String generateSellerDocumentNumber(Organization sellerOrganization) {
        // Найти последний документ для данной организации
        Document lastDocument = documentRepository.findTopBySellerOrganizationOrderByDocumentDateDesc(sellerOrganization);

        Long nextNumber;
        if (lastDocument != null && lastDocument.getDocumentNumber() != null) {
            try {
                nextNumber = Long.parseLong(lastDocument.getDocumentNumber()) + 1;
            } catch (NumberFormatException e) {
                logger.error("Ошибка преобразования номера документа в число: {}", lastDocument.getDocumentNumber(), e);
                nextNumber = 1L; // Установим значение по умолчанию, если произошла ошибка
            }
        } else {
            nextNumber = 1L;
        }

        return String.valueOf(nextNumber);
    }
//#region Lists
    /**
     *
     * Возвращает список документов по идентификатору организации продавца
     * @param sellerOrganizationId
     * @return
     */

    public List<Document> findOrdersBySellerId(Long sellerOrganizationId) {
        return documentRepository.findBySellerOrganizationId(sellerOrganizationId);
    }

    /**
     * Возвращает список документов по идентификатору организации покупателя
     * @param buyerOrganizationId
     * @return
     */
    public List<Document> findOrdersByBuyerId(Long buyerOrganizationId) {
        return documentRepository.findByBuyerOrganizationId(buyerOrganizationId);
    }

    /**
     * Находит список товаров в документе
     * @param documentId  - идентификатор документа
     * @return
     */
    public List<DocumentItem> findItemsByDocumentId(Long documentId) {
        return documentItemRepository.findItemsByDocumentId(documentId);
    }

    /**
     * Выводит список товаров по идентификатору покупателя
     * @param buyerOrganizationId - идентификатор покупателя
     * @return - список товаров
     */
    public List<DocumentItem> findDocumentItemsByBuyer(Long buyerOrganizationId) {
        List<Document> documents = documentRepository.findByBuyerOrganizationId(buyerOrganizationId);
        Map<Long, DocumentItem> documentItemMap = new HashMap<>();

        for (Document document : documents) {
            for (DocumentItem item : document.getItems()) {
                Long productId = item.getProduct().getId();
                if (documentItemMap.containsKey(productId)) {
                    DocumentItem existingItem = documentItemMap.get(productId);
                    existingItem.setQuantity(existingItem.getQuantity().add(item.getQuantity()));
                    existingItem.setAmount(existingItem.getAmount().add(item.getAmount()));
                } else {
                    DocumentItem newItem = new DocumentItem(
                            null, // or any other ID if needed
                            item.getDocumentId(),
                            item.getProduct(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getAmount()
                    );
                    documentItemMap.put(productId, newItem);
                }
            }
        }
        return new ArrayList<>(documentItemMap.values());
    }

    /** Выводит список товаров по идентификатору продавца
     *
     * @param sellerOrganizationId - идентификатор продавца
     * @return - список товаров
     */
    public List<DocumentItem> findDocumentItemsBySeller(Long sellerOrganizationId) {
        List<Document> documents = documentRepository.findBySellerOrganizationId(sellerOrganizationId);
        Map<Long, DocumentItem> documentItemMap = new HashMap<>();

        for (Document document : documents) {
            for (DocumentItem item : document.getItems()) {
                Long productId = item.getProduct().getId();
                if (documentItemMap.containsKey(productId)) {
                    DocumentItem existingItem = documentItemMap.get(productId);
                    existingItem.setQuantity(existingItem.getQuantity().add(item.getQuantity()));
                    existingItem.setAmount(existingItem.getAmount().add(item.getAmount()));
                } else {
                    DocumentItem newItem = new DocumentItem(
                            null, // or any other ID if needed
                            item.getDocumentId(),
                            item.getProduct(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getAmount()
                    );
                    documentItemMap.put(productId, newItem);
                }
            }
        }
        return new ArrayList<>(documentItemMap.values());
    }



    /**
     * Находит документ по идентификатору
     * @param documentId
     * @return
     */
    public Document findDocumentById(Long documentId) {
        return documentRepository.findById(documentId).orElse(null);
    }
    /**
     * Обновляет документ
     * @param documentId
     * @return
     */
    public Document updateDocument(Long documentId) {
        Document document = findDocumentById(documentId);
        return documentRepository.save(document);
    }
    /**
     * Удаляет документ
     * @param documentId
     * @return
     */
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }



}
