package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.exceptions.ProductNotFoundException;
import com.bev0802.salesaccounting.productdb.model.Product;
import com.bev0802.salesaccounting.productdb.servis.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST контроллер для управления товарами в базе данных.
 * Предоставляет API для создания, получения, обновления и удаления товаров.
 */

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Создает новый товар в базе данных.
     *
     * @param product Объект товара для создания.
     * @return Созданный товар.
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    /**
     * Возвращает список всех товаров из базы данных.
     *
     * @return Список товаров.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Возвращает товар по его ID.
     *
     * @param id ID товара, который нужно получить.
     * @return ResponseEntity с товаром, если он найден, или с кодом 404, если не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Возвращает список товаров, принадлежащих определенной организации.
     * @param organizationId ID организации, которой принадлежат товары.
     * @return Список товаров, принадлежащих организации.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Product>> getProductsByOrganization(@PathVariable Long organizationId) {
        List<Product> products = productService.findByOrganizationId(organizationId);
        if (products != null && !products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Возвращает список товаров, соответствующих заданному имени.
     *
     * @param name Имя товара для поиска.
     * @return Список товаров с заданным именем.
     */
    @GetMapping("/search")
    public List<Product> getProductsByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }
    /**
     * Возвращает список товаров, доступных в наличии.
     * Товары считаются доступными, если их количество больше 0.
     *
     * @return Список доступных товаров.
     */

    @GetMapping("/available")
    public List<Product> getAvailableProducts() {
        return productService.getAvailableProducts();
    }

    /**
     * Обновляет информацию о существующем товаре в базе данных.
     * Если товар с указанным ID не найден, возвращает статус 404 Not Found.
     *
     * @param id ID товара для обновления.
     * @param productDetails Детали товара для обновления.
     * @return Обновленный товар, или статус 404, если товар не найден.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        Product updatedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Возвращает список товаров, цена которых находится в заданном диапазоне.
     *
     * @param from Нижняя граница диапазона цены.
     * @param to Верхняя граница диапазона цены.
     * @return Список товаров с ценой в указанном диапазоне.
     */
    @GetMapping("/by-price")
    public List<Product> getProductsByPriceRange(@RequestParam BigDecimal from, @RequestParam BigDecimal to) {
        return productService.getProductsByPriceRange(from, to);
    }

    /**
     * Возвращает список товаров, соответствующих заданным критериям фильтрации.
     * Параметры фильтрации включают имя товара, наличие на складе, и диапазон цен.
     *
     * @param name Имя товара для поиска.
     * @param available Фильтр по наличию товара на складе.
     * @param startPrice Начальная граница диапазона цен.
     * @param endPrice Конечная граница диапазона цен.
     * @return Список товаров, соответствующих заданным критериям.
     */
    @GetMapping("/filtered")
    public List<Product> findProducts(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) Boolean available,
                                      @RequestParam(required = false) BigDecimal startPrice,
                                      @RequestParam(required = false) BigDecimal endPrice) {
        return productService.findProducts(name, available, startPrice, endPrice);}

    /**
     * Удаляет товар из базы данных по его ID.
     *
     * @param id ID товара для удаления.
     * @return ResponseEntity без тела, с кодом 200, если товар удален, или с кодом 404, если не найден.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Обрабатывает исключение ProductNotFoundException.
     * Возвращает сообщение об ошибке и статус 404 Not Found.
     *
     * @param ex Исключение, содержащее информацию об ошибке.
     * @return Сообщение об ошибке.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(ProductNotFoundException ex) {
        return ex.getMessage();
    }
}
