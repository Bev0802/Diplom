package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Product;
import com.bev0802.salesaccounting.productdb.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void whenGetAllProducts_thenProductListReturned() {
        // Подготовка
        Product product = new Product();
        product.setName("Test Product");
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        // Действие
        List<Product> products = productService.getAllProducts();

        // Проверка
        assertFalse(products.isEmpty());
        assertEquals("Test Product", products.get(0).getName());

        // Проверяем, что репозиторий был вызван ровно один раз
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void saveProduct() {
        Product product = new Product();
        product.setName("New Product");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(product, null);

        assertNotNull(savedProduct);
        assertEquals("New Product", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void getProductsByName() {
        Product product = new Product();
        product.setName("Test");
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getProductsByName("Test");

        assertFalse(products.isEmpty());
        assertEquals("Test", products.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }


    @Test
    void getAvailableProducts() {
        Product product = new Product();
        product.setQuantity(BigDecimal.valueOf(5));
        when(productRepository.findByQuantityGreaterThan(BigDecimal.ZERO)).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAvailableProducts();

        assertFalse(products.isEmpty());
        assertTrue(products.get(0).getQuantity().compareTo(BigDecimal.ZERO) > 0);
        verify(productRepository, times(1)).findByQuantityGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void getProductsByPriceRange() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(50));
        when(productRepository.findByPriceBetween(BigDecimal.valueOf(30), BigDecimal.valueOf(70))).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getProductsByPriceRange(BigDecimal.valueOf(30), BigDecimal.valueOf(70));

        assertFalse(products.isEmpty());
        assertTrue(products.get(0).getPrice().compareTo(BigDecimal.valueOf(30)) >= 0 &&
                products.get(0).getPrice().compareTo(BigDecimal.valueOf(70)) <= 0);
        verify(productRepository, times(1)).findByPriceBetween(BigDecimal.valueOf(30), BigDecimal.valueOf(70));
    }

    @Test
    void findProducts() {
        Product product = new Product();
        product.setName("Test");
        product.setQuantity(BigDecimal.TEN);

        // Имитируем возвращаемое значение для любого вызова findAll с любой Specification
        when(productRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.findProducts("Test", true, null, null);

        assertFalse(products.isEmpty());
        assertEquals("Test", products.get(0).getName());
        assertTrue(products.get(0).getQuantity().compareTo(BigDecimal.ZERO) > 0);

        // Проверяем, что метод findAll был вызван с любой спецификацией
        verify(productRepository, times(1)).findAll(any(Specification.class));
    }


    @Test
    void deleteProduct_success() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(BigDecimal.ZERO);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void updateProduct() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");
        Product updatedDetails = new Product();
        updatedDetails.setName("New Name");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);

        Product updatedProduct = productService.updateProduct(productId, updatedDetails);

        assertNotNull(updatedProduct);
        assertEquals("New Name", updatedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

}