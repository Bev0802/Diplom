package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.exception.ServiceException;
import com.bev0802.salesaccounting.wholesale.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для управления продуктами через взаимодействие с REST API сервера productDB.
 * Предоставляет функциональность для выполнения CRUD операций над продуктами,
 * а также для поиска продуктов по различным критериям.
 */
@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final RestTemplate restTemplate;

    @Value("${productDB.service.url}")
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
            Product[] products = restTemplate.getForObject(productServiceUrl + "/api/products", Product[].class);
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
     * @param id Идентификатор продукта.
     * @return Продукт, если он найден.
     * @throws ServiceException если продукт не найден или запрос не удался.
     */
    public Product getProductById(Long id) {
        try {
            return restTemplate.getForObject(productServiceUrl + "/api/products/{id}", Product.class, id);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении товара по идентификатору.: {}", id, e);
            throw new ServiceException("Не удалось получить товар по идентификатору. " + id + ": " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Ошибка при получении товара по идентификатору.: {}", id, e);
            throw new ServiceException("Произошла ошибка при получении товара по идентификатору. " + id);
        }
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта для удаления.
     * @throws ServiceException если продукт не может быть удален.
     */
    public void deleteProduct(Long id) {
        restTemplate.delete(productServiceUrl + "/api/products/{id}", id);

    }

    /**
     * Поиск продуктов по совпадению слова в названии.
     *
     * @param name Имя или его часть для поиска продуктов.
     * @return Список продуктов, соответствующих критерию поиска.
     * @throws ServiceException если запрос не удался.
     */

    public List<Product> findProductsByName(String name) {
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/api/products/search")
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
            Product[] products = restTemplate.getForObject(productServiceUrl + "/api/products/available", Product[].class);
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
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/api/products/by-price")
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
        URI uri = UriComponentsBuilder.fromUriString(productServiceUrl + "/api/products/filtered")
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
     * @param id Идентификатор продукта для копирования.
     * @return Копия продукта, сохраненная как новый продукт.
     * @throws ServiceException если продукт для копирования не найден или запрос на создание копии не удался.
     */
    public Product cloneProduct(Long id) {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
            throw new ServiceException("Продукт для клонирования не найден");
        }
        Product newProduct = new Product();
        // Копируем все поля, кроме ID и количества
        newProduct.setName(existingProduct.getName());
        newProduct.setPrice(existingProduct.getPrice());

        // Обнуляем количество товара для нового продукта
        newProduct.setQuantity(BigDecimal.ZERO); // или другое значение по умолчанию, если нужно

        return createProduct(newProduct);
    }
    /**
     * Создает новый товар.
     * @param product Продукт для создания.
     */
        public Product createProduct(Product product) {
        try {
            return restTemplate.postForObject(productServiceUrl + "/api/products", product, Product.class);
        } catch (Exception e) {
            logger.error("Ошибка создания продукта", e);
            throw new ServiceException("Произошла ошибка при создании продукта.");
        }
    }
    /**
     * Обновляет товар.
     * @param product Товар для сохранения.
     */
    private void updateProduct(Product product) {
        restTemplate.put(productServiceUrl + "/api/products/" + product.getId(), product);
    }
    /**
     * Создает новый продукт или обновляет существующий в зависимости от наличия идентификатора.
     *
     * @param product Продукт для создания или обновления.
     */
    public void saveOrUpdateProduct(Product product) {
        if (product.getId() == null) {
            // Создание нового продукта
            product.setQuantity(BigDecimal.ZERO);
            restTemplate.postForObject(productServiceUrl + "/api/products", product, Product.class);
        } else {
            // Обновление существующего продукта
            restTemplate.put(productServiceUrl + "/api/products/" + product.getId(), product);
        }
    }
}

