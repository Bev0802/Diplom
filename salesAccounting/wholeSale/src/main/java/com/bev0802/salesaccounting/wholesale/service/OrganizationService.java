package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.exception.ServiceException;
import com.bev0802.salesaccounting.wholesale.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Сервис для работы с организациями.
 */
@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    private final RestTemplate restTemplate;

    @Value("${productDB.service.url}/api/organization/")
    private String productDBServiceUrl;

    /**
     * Конструктор класса OrganizationService.
     *
     * @param restTemplate RestTemplate для выполнения HTTP-запросов.
     */
    @Autowired
    public OrganizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Получает список всех организаций.
     *
     * @return Список всех организаций.
     * @throws ServiceException Если возникает ошибка при получении организаций.
     */
    public List<Organization> findAll() {
        try {
            Organization[] organizations = restTemplate.getForObject(productDBServiceUrl, Organization[].class);
            assert organizations != null;
            return Arrays.asList(organizations);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении всех организаций.", e);
            throw new ServiceException("Не удалось получить организации: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Ошибка при получении всех организаций.", e);
            throw new ServiceException("Произошла ошибка при получении организаций..");
        }
    }

    /**
     * Получает организацию по её идентификатору.
     *
     * @param organizationId Идентификатор организации.
     * @return Организация с указанным идентификатором.
     * @throws ServiceException Если возникает ошибка при получении организации.
     */
    public Organization findById(Long organizationId) {
        try {
            return restTemplate.getForObject(productDBServiceUrl + organizationId, Organization.class);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении организации.", e);
            throw new ServiceException("Не удалось получить организацию: " + e.getResponseBodyAsString());
        }
    }

    /**
     * Получает список контрагентов (организаций) для указанной организации, исключая текущую организацию.
     * @param organizationId Идентификатор текущей организации.
     * @return Список контрагентов (организаций).
     * @throws ServiceException Если возникает ошибка при получении списка контрагентов.
     */
    /**
     * Получает список контрагентов (организаций) для указанной организации, исключая текущую организацию.
     * @param organizationId Идентификатор текущей организации.
     * @return Список контрагентов (организаций).
     * @throws ServiceException Если возникает ошибка при получении списка контрагентов.
     */
    public List<Organization> listCounterparty(Long organizationId) {
        List<Organization> allOrganizations = findAll();
        // Создаем копию списка
        List<Organization> counterparties = new ArrayList<>(allOrganizations);
        // Удаление организации с переданным ID из списка
        counterparties.removeIf(org -> org.getId().equals(organizationId));
        return counterparties;
    }

    /**
     * Получает детали контрагента (организации) по её идентификатору.
     *
     * @param organizationId Идентификатор организации.
     * @return Детали контрагента (организации).
     * @throws ServiceException Если возникает ошибка при получении деталей контрагента.
     */
    public Organization detailCounterparty(Long organizationId) {
        try {
            return restTemplate.getForObject(productDBServiceUrl + organizationId, Organization.class);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении данных контрагента.", e);
            throw new ServiceException("Не удалось получить данные контрагента: " + e.getResponseBodyAsString());
        }
    }
}

