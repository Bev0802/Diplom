package com.bev0802.salesaccounting.productdb.servis;

import com.bev0802.salesaccounting.productdb.exceptions.ProductInStockException;
import com.bev0802.salesaccounting.productdb.exceptions.ProductNotFoundException;
import com.bev0802.salesaccounting.productdb.model.Product;
import com.bev0802.salesaccounting.productdb.repository.ProductRepository;
import com.bev0802.salesaccounting.productdb.repository.specification.ProductSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.List;
/**
 * Сервис для управления продуктами в базе данных.
 * Предоставляет методы для создания, поиска, обновления и удаления продуктов.
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    /**
     * Сохраняет продукт в базе данных.
     *
     * @param product Продукт для сохранения.
     * @return Сохраненный продукт.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    /**
     * Возвращает список всех продуктов из базы данных.
     *
     * @return Список продуктов.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    /**
     * Находит продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Найденный продукт или null, если продукт не найден.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    /**
     * Возвращает список продуктов, содержащих заданное имя, не учитывая регистр.
     *
     * @param name Имя для поиска в названиях продуктов.
     * @return Список найденных продуктов.
     */
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    /**
     * Возвращает список продуктов, количество которых больше нуля.
     *
     * @return Список продуктов в наличии.
     */
    public List<Product> getAvailableProducts() {
        return productRepository.findByQuantityGreaterThan(BigDecimal.valueOf(0));
    }
    /**
     * Возвращает список продуктов с ценой в указанном диапазоне.
     *
     * @param priceStart Начальная цена диапазона.
     * @param priceEnd Конечная цена диапазона.
     * @return Список продуктов в заданном диапазоне цен.
     */
    public List<Product> getProductsByPriceRange(BigDecimal priceStart, BigDecimal priceEnd) {
        return productRepository.findByPriceBetween(priceStart, priceEnd);
    }
    /**
     * Находит продукты, соответствующие заданным критериям фильтрации.
     *
     * @param name Имя продукта для поиска.
     * @param available Фильтр по наличию продукта.
     * @param startPrice Начальная цена для фильтрации.
     * @param endPrice Конечная цена для фильтрации.
     * @return Список продуктов, соответствующих заданным критериям.
     */
    public List<Product> findProducts(String name, Boolean available, BigDecimal startPrice, BigDecimal endPrice) {
        // Начинаем с неограниченной спецификации
        Specification<Product> spec = Specification.where(null);

        // Фильтр по имени
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(ProductSpecifications.hasNameLike(name));
        }

        // Фильтр по наличию
        if (available != null) {
            if (available) {
                // Добавляем условие, что количество товара больше нуля
                spec = spec.and(ProductSpecifications.quantityGreaterThanZero());
            } else {
                // Добавляем условие, что количество товара равно нулю или товар не указан как доступный
                spec = spec.and(ProductSpecifications.quantityEqualToZero());
            }
        }

        // Фильтр по цене
        if (startPrice != null && endPrice != null) {
            spec = spec.and(ProductSpecifications.priceBetween(startPrice, endPrice));
        }

        return productRepository.findAll(spec);
    }

    /**
     * Удаляет продукт из базы данных по его идентификатору.
     *
     * @param id Идентификатор продукта для удаления.
     * @throws ProductNotFoundException если продукт не найден.
     * @throws ProductInStockException если продукт в наличии и не может быть удален.
     */
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        if (product.getQuantity() != null && product.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
            throw new ProductInStockException("Product with id " + id + " is in stock and cannot be deleted");
        }
//        if (product.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
//            throw new ProductInStockException("Product with id " + id + " is in stock and cannot be deleted");
//        }
        log.info("Deleting product: {}", product);
        productRepository.delete(product);
    }
    /**
     * Обновляет информацию о продукте в базе данных.
     *
     * @param id Идентификатор продукта для обновления.
     * @param productDetails Новые детали продукта для сохранения.
     * @return Обновленный продукт.
     * @throws ProductNotFoundException если продукт не найден.
     */
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    log.info("Updating product: {}", existingProduct);
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    /**
     * Возвращает список товаров, принадлежащих определенной организации.
     * @param organizationId ID организации, которой принадлежат товары.
     * @return Список товаров, принадлежащих организации.
     */
    public List<Product> findByOrganizationId(Long organizationId) {
        return productRepository.findByOrganizationId(organizationId);
    }
}


