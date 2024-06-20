package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.*;
import com.bev0802.salesaccounting.productdb.repository.DocumentItemRepository;
import com.bev0802.salesaccounting.productdb.repository.DocumentRepository;
import com.bev0802.salesaccounting.productdb.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для DocumentService.
 */
class DocumentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ProductService productService;

    @Mock
    private DocumentItemRepository documentItemRepository;

    @InjectMocks
    private DocumentService documentService;
    /**
     * Инициализация моков перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Тест для метода createDocumentFromOrder.
     */
    @Test
    void createDocumentFromOrder() {
        // Создание и инициализация заказа
        Order order = new Order();
        order.setId(1L);
        order.setBuyerOrganization(new Organization());
        order.setSellerOrganization(new Organization());
        order.setEmployee(new Employee());
        order.setTotalAmount(BigDecimal.valueOf(1000.0)); // Используем BigDecimal.valueOf

        // Инициализация списка OrderItems
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(new Product());
        orderItem1.setQuantity(BigDecimal.valueOf(2));
        orderItem1.setPrice(BigDecimal.valueOf(500));
        orderItem1.setAmount(BigDecimal.valueOf(1000));
        order.setItems(new HashSet<>(Arrays.asList(orderItem1))); // Инициализация списка OrderItems

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(documentRepository.save(any(Document.class))).thenReturn(new Document());
        when(documentItemRepository.save(any(DocumentItem.class))).thenReturn(new DocumentItem());

        Document document = documentService.createDocumentFromOrder(1L);

        assertNotNull(document);
        verify(orderRepository, times(1)).findById(1L);
        verify(documentRepository, times(2)).save(any(Document.class));
        verify(documentItemRepository, times(1)).save(any(DocumentItem.class));
    }
    /**
     * Тест для метода generateSellerDocumentNumber.
     */
    @Test
    void generateSellerDocumentNumber() {
        Organization sellerOrganization = new Organization();
        Document lastDocument = new Document();
        lastDocument.setDocumentNumber("10");

        when(documentRepository.findTopBySellerOrganizationOrderByDocumentDateDesc(sellerOrganization)).thenReturn(lastDocument);

        String newDocumentNumber = documentService.generateSellerDocumentNumber(sellerOrganization);

        assertEquals("11", newDocumentNumber);
    }

    @Test
    void findOrdersBySellerId() {
        when(documentRepository.findBySellerOrganizationId(1L)).thenReturn(Arrays.asList(new Document(), new Document()));

        List<Document> documents = documentService.findOrdersBySellerId(1L);

        assertEquals(2, documents.size());
    }
    /**
     * Тест для метода findOrdersBySellerId.
     */
    @Test
    void findOrdersByBuyerId() {
        when(documentRepository.findByBuyerOrganizationId(1L)).thenReturn(Arrays.asList(new Document(), new Document()));

        List<Document> documents = documentService.findOrdersByBuyerId(1L);

        assertEquals(2, documents.size());
    }
    /**
     * Тест для метода findOrdersByBuyerId.
     */
    @Test
    void findItemsByDocumentId() {
        when(documentItemRepository.findItemsByDocumentId(1L)).thenReturn(Arrays.asList(new DocumentItem(), new DocumentItem()));

        List<DocumentItem> documentItems = documentService.findItemsByDocumentId(1L);

        assertEquals(2, documentItems.size());
    }
    /**
     * Тест для метода findItemsByDocumentId.
     */
    @Test
    void findDocumentById() {
        Document document = new Document();
        document.setId(1L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        Document foundDocument = documentService.findDocumentById(1L);

        assertNotNull(foundDocument);
        assertEquals(1L, foundDocument.getId());
    }
    /**
     * Тест для метода findDocumentById.
     */
    @Test
    void updateDocument() {
        Document document = new Document();
        document.setId(1L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        Document updatedDocument = documentService.updateDocument(1L);

        assertNotNull(updatedDocument);
        assertEquals(1L, updatedDocument.getId());
    }
    /**
     * Тест для метода updateDocument.
     */
    @Test
    void deleteDocument() {
        documentService.deleteDocument(1L);
        verify(documentRepository, times(1)).deleteById(1L);
    }
}
