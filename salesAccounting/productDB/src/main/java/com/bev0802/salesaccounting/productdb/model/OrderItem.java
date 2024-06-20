package com.bev0802.salesaccounting.productdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/**
 * Модель товарной позиции в заказе.
 * Содержит информацию о её идентификаторе, ссылке на заказ, товаре, количестве, цене, сумме и методе для получения идентификатора продукта.
 */
@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ссылка на идентификатор заказа.
     */
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Long orderId;

    /**
     * Ссылка на товар
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Количество товара
     */
    private BigDecimal quantity;

    /**
     * Цена товара
     */
    private BigDecimal price;


    /**
    * Сумма товара вычисляемая из количества и цены
    */
    private BigDecimal amount;
    /**
     * Метод для вычисления суммы товара перед сохранением в базу данных.
     * Вызывается автоматически перед сохранением и обновлением записи.
     */
    @PrePersist
    @PreUpdate
    public void calculateAmount() {
        if (price != null && quantity != null) {
            this.amount = price.multiply(quantity);
        } else {
            this.amount = BigDecimal.ZERO;
        }
    }

    /**
     * Метод для получения идентификатора продукта
     */
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
}