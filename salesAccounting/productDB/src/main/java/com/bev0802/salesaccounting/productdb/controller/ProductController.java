package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.exceptions.ProductNotFoundException;
import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.model.Product;
import com.bev0802.salesaccounting.productdb.service.ProductService;
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
@RequestMapping("/api/organization/{organizationId}/employee/{employeeId}/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Создает новый товар в базе данных.
     *
     * @param product Объект товара для создания.
     * @param organizationId ID организации, к которой будет привязан товар.
     * @return Созданный товар.
     */
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product, @PathVariable Long organizationId) {
        Product createdProduct = productService.saveProduct(product, organizationId);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Возвращает список всех товаров из базы данных.
     *
     * @return Список товаров.
     */
    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Возвращает товар по его ID.
     *
     * @param productId ID товара, который нужно получить.
     * @return ResponseEntity с товаром, если он найден, или с кодом 404, если не найден.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
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
    @GetMapping("/listByOrganization/")
    public ResponseEntity<List<Product>> getProductsByOrganization(@PathVariable Long organizationId) {
        List<Product> products = productService.findByOrganizationId(organizationId);
        if (products != null && !products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Находит организащию, которой прнадлежит товар с заданным ID
     * @param productId
     * @return Организация
     */
    @GetMapping("/getOrganizationByProductId/{productId}")
    public ResponseEntity<Organization> getOrganizationByProductId(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product.getOrganization());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Возвращает список товаров, доступных для покупки в определенной организации.
     * @param organizationId ID организации, для которой нужно получить доступные для покупки
     * @return Список доступных для покупки
     */
    @GetMapping("/availableForPurchase")
    public ResponseEntity<List<Product>> getProductsAvailableForPurchaseByOrganizationId(@PathVariable Long organizationId) {
        List<Product> products = productService.findProductsNotBelongingToOrganization(organizationId);
        return ResponseEntity.ok(products);
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
     * @param productId ID товара для обновления.
     * @param productDetails Детали товара для обновления.
     * @return Обновленный товар, или статус 404, если товар не найден.
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product productDetails) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());

        Product updatedProduct = productService.saveProduct(product, product.getOrganization().getId());
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
     * Удаляет товар из базы данных по его ID, если его количество больше 0.
     *
     * @param productId ID товара для удаления.
     * @return ResponseEntity без тела, с кодом 200, если товар удален, или с кодом 404, если не найден.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            productService.deleteProduct(productId);
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
