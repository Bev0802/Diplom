package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Document;
import com.bev0802.salesaccounting.wholesale.model.DocumentItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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
     * Получает список реализации, по идентификатору организации продавца и сотрудника
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

    /**
     * Получает список поступлений, по идентификатору организации покупателя и сотрудника
     * @param organizationId
     * @param employeeId
     * @return
     */
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
    //TODO: создать на севресе методы /ListDocumentItemsByBuyer и /ListDocumentItemsBySeller

    /**
     * Получает список купленных товаров, по идентификатору организации покупателя и сотрудника
     * @param organizationId - идентификатор организации покупателя
     * @param employeeId
     * @return
     */
    public List<DocumentItem> findDocumentItemsByBuyer(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/ListDocumentItemsByBuyer";
        logger.info("URL: " + url);
        try {
            DocumentItem[] documentItems = restTemplate.getForObject(url, DocumentItem[].class);
            assert documentItems != null;
            return Arrays.asList(documentItems);
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }

    /**
     * Получает список проданных товаров, по идентификатору организации продавца и сотрудника
     * @param organizationId
     * @param employeeId
     * @return
     */
    public List<DocumentItem> findDocumentItemsBySeller(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/ListDocumentItemsBySeller";
        logger.info("URL: " + url);
        try {
            DocumentItem[] documentItems = restTemplate.getForObject(url, DocumentItem[].class);
            assert documentItems != null;
            return Arrays.asList(documentItems);
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }


    /**
     * Получает документ по идентификатору
     * @param documentId
     * @param organizationId
     * @param employeeId
     * @return
     */
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


    //region: Фильтрация

//TODO: Доделать отбор не сервере создать методы: /filteredSearchDocument и /filteredSearchDocumentItems
    /**
     * Поиск и отбор документов по фильтрам
     * @param organizationId - идентификатор организации
     * @param employeeId - идентификатор сотрудника
     * @param documentNumber - номер документа
     * @param buyer - имя покупателя
     * @param seller - имя продавца
     * @param dateFrom - дата начала отбора
     * @param dateTo
     * @return
     */

    public List<Document> findDocumentsFiltered(Long organizationId, Long employeeId, String documentNumber, String buyer, String seller, LocalDateTime dateFrom, LocalDateTime dateTo) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/filteredSearchDocument?documentNumber=" + documentNumber + "&seller=" + seller + "&buyer=" + buyer + "&dateFrom=" + dateFrom + "&dateTo=" + dateTo;
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

    /**
     * Поиск и отбор продонных(купленных) товаров из документов по фильтрам
     * @param organizationId - идентификатор организации
     * @param employeeId - идентификатор сотрудника
     * @param name - название продукта
     * @param seller - имя продавца
     * @param buyer - имя покупателя
     * @param dateFrom - дата начала отбора
     * @param dateTo - дата окончания отбора
     * @return
     */

    public List<DocumentItem> findDocumentItemsFiltered(Long organizationId,
                                                        Long employeeId,
                                                        String name,
                                                        String seller,
                                                        String buyer,
                                                        LocalDateTime dateFrom,
                                                        LocalDateTime dateTo) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/document/filteredSearchDocument?name=" + name + "&seller=" + seller + "&buyer=" + buyer + "&dateFrom=" + dateFrom + "&dateTo=" + dateTo;
        logger.info("URL: " + url);
        try {
            DocumentItem[] documentItems = restTemplate.getForObject(url, DocumentItem[].class);
            assert documentItems != null;
            return Arrays.asList(documentItems);
        } catch (RestClientException e) {
            logger.error("Ошибка при извлечении ответа из службы ProductDB.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при анализе ответа JSON от службы ProductDB.", e);
            throw new RuntimeException("Ошибка при анализе ответа JSON", e);
        }
    }


    //endregion: Фильтрация
}
