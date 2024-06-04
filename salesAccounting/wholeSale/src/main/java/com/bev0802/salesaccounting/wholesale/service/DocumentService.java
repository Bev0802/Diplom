package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Сервис для работы с документами
 */
@Service
public class DocumentService {
    @Value("${productDB.service.url}")
    private String productDBServiceUrl;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    public DocumentService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param organizationId
     * @param employeeId
     * @return
     */
    public List<Document> findDocumentsBySellerId(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/sellerList";
        logger.info("URL: " + url);
        try {
            Document[] documents = restTemplate.getForObject(url, Document[].class);
            assert documents != null;
            return Arrays.asList(documents);
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }

    public List<Document> findDocumentsByBuyerId(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/buyerList";
        logger.info("URL: " + url);
        try {
            Document[] documents = restTemplate.getForObject(url, Document[].class);
            assert documents != null;
            return Arrays.asList(documents);
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }

    public Document findById(Long documentId, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/" + documentId;
        logger.info("URL: " + url);
        try {
            Document document = restTemplate.getForObject(url, Document.class);
            assert document != null;
            return document;
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }
}
