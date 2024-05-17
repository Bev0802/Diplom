package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.exception.ServiceException;
import com.bev0802.salesaccounting.wholesale.model.Organization;
import com.bev0802.salesaccounting.wholesale.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления продуктами через взаимодействие с REST API сервера productDB.
 * Предоставляет функциональность для выполнения CRUD операций над продуктами,
 * а также для поиска продуктов по различным критериям.
 */
@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final RestTemplate restTemplate;

    @Value("${productDB.service.url}/api/organization/{organizationId}/employee/{employeeId}/product")
    private String productServiceUrl;

    /**
     * Конструктор для внедрения зависимости RestTemplate.
     *
     * @param restTemplate Клиент для выполнения HTTP запросов.
     */
    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    /**
     * Получает список всех доступных продуктов с внешнего сервиса.
     *
     * @return Список продуктов.
     * @throws ServiceException в случае ошибки во время выполнения запроса.
     */
    public List<Product> getAllProducts() {
        try {
            Product[] products = restTemplate.getForObject(productServiceUrl, Product[].class);
            assert products != null;
            return Arrays.asList(products);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении всех товаров.", e);
            throw new ServiceException("Не удалось получить товары: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Ошибка при получении всех товаров.", e);
            throw new ServiceException("Произошла ошибка при получении товаров..");
        }
    }
    /**
     * Возвращает продукт по его уникальному идентификатору.
     *
     * @param productId Идентификатор продукта.
     * @return Продукт, если он найден.
     * @throws ServiceException если продукт не найден или запрос не удался.
     */
    public Product getProductById(Long productId, Long organizationId, Long employeeId) {
        try {
            // Формируем URL, используя UriComponentsBuilder для добавления пути и переменных
            String url = UriComponentsBuilder
                    .fromUriString(productServiceUrl)  // Здесь должен быть базовый URL до endpoint
                    .path("/{productId}")  // Добавляем к URL путь к конкретному продукту
                    .buildAndExpand(Map.of("productId", productId, "organizationId", organizationId, "employeeId", employeeId))
                    .toUriString();

            // Отправляем запрос и получаем продукт
            return restTemplate.getForObject(url, Product.class);
        } catch (HttpClientErrorException e) {
            // Логирование ошибки и выброс исключения ServiceException с сообщением и причиной
            logger.error("Ошибка при получении товара по идентификатору: {}", productId, e);
            throw new ServiceException("Не удалось получить товар по идентификатору " + productId + ": " + e.getResponseBodyAsString(), e);
        }
    }


    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param productId Идентификатор продукта для удаления.
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @throws ServiceException если продукт не может быть удален.
     */
    public void deleteProduct(Long productId, Long organizationId, Long employeeId) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(productServiceUrl)  // Здесь должен быть базовый URL до endpoint
                    .path("/{productId}")  // Добавляем к URL путь к конкретному продукту
                    .buildAndExpand(Map.of("productId", productId, "organizationId", organizationId, "employeeId", employeeId))
                    .toUriString();

            restTemplate.delete(url);

        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при удалении продукта с ID: {}, Организация ID: {}, Сотрудник ID: {}", productId, organizationId, employeeId, e);
            throw new ServiceException("Ошибка доступа. Недостаточно прав для выполнения операции.");
        }
    }

    /**
     * Поиск продуктов по совпадению слова в названии.
     *
     * @param name Имя или его часть для поиска продуктов.
     * @return Список продуктов, соответствующих критерию поиска.
     * @throws ServiceException если запрос не удался.
     */

    public List<Product> findProductsByName(String name) {
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/search")
                .queryParam("name", name)
                .build()
                .encode() // Добавьте этот вызов для кодирования URL
                .toUri();
        try {
            Product[] products = restTemplate.getForObject(uri, Product[].class);
            logger.info("Найдено продуктов: {}", products != null ? products.length : 0);
            return Arrays.asList(products);
        } catch (Exception e) {
            logger.error("Ошибка поиска товаров по названию.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Возвращает список продуктов, которые доступны в наличии.
     *
     * @return Список доступных продуктов.
     * @throws ServiceException если запрос не удался.
     */
    public List<Product> findAvailableProducts() {
        try {
            Product[] products = restTemplate.getForObject(productServiceUrl + "/available", Product[].class);
            assert products != null;
            return Arrays.asList(products);
        } catch (Exception e) {
            logger.error("Ошибка при получении доступных продуктов.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Поиск продуктов по диапазону цен.
     *
     * @param startPrice Начальная цена диапазона.
     * @param endPrice Конечная цена диапазона.
     * @return Список продуктов в заданном диапазоне цен.
     * @throws ServiceException если запрос не удался.
     */
    public List<Product> findProductsByPriceRange(BigDecimal startPrice, BigDecimal endPrice) {
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/by-price")
                .queryParam("from", startPrice)
                .queryParam("to", endPrice)
                .build()
                .toUri();
        try {
            Product[] products = restTemplate.getForObject(uri, Product[].class);
            return Arrays.asList(products);
        } catch (Exception e) {
            logger.error("Ошибка поиска товаров по ценовому диапазону.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Множественный поиск продуктов с использованием различных фильтров.
     *
     * @param name Имя продукта.
     * @param available Доступность продукта.
     * @param startPrice Начальная цена.
     * @param endPrice Конечная цена.
     * @return Список продуктов, соответствующих заданным критериям.
     * @throws ServiceException если запрос не удался.
     */
    public List<Product> findProductsFiltered(String name, Boolean available, BigDecimal startPrice, BigDecimal endPrice) {
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/filtered")
                .queryParam("name", name)
                .queryParam("available", available)
                .queryParam("startPrice", startPrice)
                .queryParam("endPrice", endPrice)
                .build()
                .encode()
                .toUri();
        try {
            Product[] products = restTemplate.getForObject(uri, Product[].class);
            assert products != null;
            return Arrays.asList(products);
        } catch (Exception e) {
            logger.error("Ошибка поиска товаров с несколькими фильтрами", e);
            return Collections.emptyList();
        }
    }

    /**
     * Создает копию существующего продукта по его ID. Новая копия будет сохранена как новый продукт с уникальным ID.
     *
     * @param productId Идентификатор продукта для копирования.
     * @return Копия продукта, сохраненная как новый продукт.
     * @throws ServiceException если продукт для копирования не найден или запрос на создание копии не удался.
     */
    public Product cloneProduct(Long productId, Long organizationId, Long employeeId) {
        Product existingProduct = getProductById(productId, organizationId, employeeId);
        if (existingProduct == null) {
            throw new ServiceException("Продукт для клонирования не найден");
        }
        Product newProduct = new Product();

        // Копируем все поля, кроме ID и количества
        newProduct.setName(existingProduct.getName());
        newProduct.setPrice(existingProduct.getPrice());

        // Обнуляем количество товара для нового продукта
        newProduct.setQuantity(BigDecimal.ZERO); // или другое значение по умолчанию, если нужно
        //newProduct.setQuantity(existingProduct.getQuantity());

        return createProduct(newProduct, organizationId, employeeId);
    }
    /**
     * Создает новый товар.
     * @param product Продукт для создания.
     * @param organizationId Идентификатор организации, к которой принадлежит продукт.
     * @param employeeId Идентификатор сотрудника, создающего продукт.
     * @return Созданный продукт.
     */
    public Product createProduct(Product product, Long organizationId, Long employeeId) {
        try {
            // Формирование URL с подстановкой идентификаторов организации и сотрудника
            String url = UriComponentsBuilder.fromUriString(productServiceUrl + "/create")
                    .queryParam("organizationId", organizationId)
                    .queryParam("employeeId", employeeId)
                    .toUriString();

            // Создание заголовков для запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Создание тела запроса, включая продукт
            HttpEntity<Product> request = new HttpEntity<>(product, headers);

            // Отправка POST запроса с продуктом и получение результата
            ResponseEntity<Product> response = restTemplate.postForEntity(url, request, Product.class);

            // Проверка статуса ответа и возврат созданного продукта
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new ServiceException("Не удалось создать продукт: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка создания продукта: {}", e.getResponseBodyAsString());
            throw new ServiceException("Ошибка создания продукта: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Ошибка при создании продукта", e);
            throw new ServiceException("Произошла ошибка при создании продукта.", e);
        }
    }
    /**
     * Обновляет товар.
     * @param product Товар для сохранения.
     */
    private void updateProduct(Product product) {
        restTemplate.put(productServiceUrl + product.getId(), product);
    }
    /**
     * Создает новый продукт или обновляет существующий в зависимости от наличия идентификатора.
     *
     * @param product Продукт для создания или обновления.
     */
    public void saveOrUpdateProduct(Product product, Long organizationId, Long employeeId) {
        if (product.getId() == null) {
            // Создание нового продукта
            //product.setQuantity(BigDecimal.ZERO); // установите начальное количество, если нужно
            String url = UriComponentsBuilder.fromUriString(productServiceUrl + "/create")
                    .buildAndExpand(Map.of("organizationId", organizationId, "employeeId", employeeId))
                    .toUriString();
            restTemplate.postForObject(url, product, Product.class);
        } else {
            // Обновление существующего продукта
            String url = UriComponentsBuilder.fromUriString(productServiceUrl + "/{productId}")
                    .buildAndExpand(Map.of("productId", product.getId(), "organizationId", organizationId, "employeeId", employeeId))
                    .toUriString();
            restTemplate.put(url, product);
        }
    }


    /**
     * Получает список продуктов по идентификатору организации.
     *
     * @return взвращает список товаров принадлежащих организации для продажи
     */
    public List<Product> findProductsByOrganization(Long organizationId, Long employeeId) {
        String url = UriComponentsBuilder.fromUriString(productServiceUrl + "/listByOrganization/")
                .buildAndExpand(Map.of("organizationId", organizationId, "employeeId", employeeId))
                .toUriString();

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});
        return response.getBody();
    }

    /**
     * Получает список товаров, доступных для покупки в определенной организации.
     * @param organizationId идетификатор оргаризации
     * @return возвращает список доступных для покупки
     */

    public List<Product> findProductsNotBelongingToOrganization(Long organizationId) {
        String url = productServiceUrl + "/availableForPurchase/";
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {});
        return response.getBody();
    }

    /**
     * Получает организацию по идентификатору товара.
     * @param productId
     * @return возвращает организацию
     */
    public Organization getOrganizationByProductId(Long productId) {
        String url = productServiceUrl + "/getOrganizationByProductId/" + productId;
        ResponseEntity<Organization> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Organization>() {});
        return response.getBody();
    }

}

