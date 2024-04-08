package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Organization;
import com.bev0802.salesaccounting.wholesale.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final RestTemplate restTemplate;

    @Value("${productDB.service.url}")
    private String productDBServiceUrl;

    @Autowired
    public OrganizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Organization> findAll() {
        try {
            Organization[] organizations = restTemplate.getForObject(productDBServiceUrl + "/api/organizations", Organization[].class);
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

    public Organization findById(Long organizationId) {
        try {
            return restTemplate.getForObject(productDBServiceUrl + "/api/organizations/" + organizationId, Organization.class);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении организации.", e);
            throw new ServiceException("Не удалось получить организацию: " + e.getResponseBodyAsString());
        }
    }
}

