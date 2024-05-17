package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ссылка на заказ
     * */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

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
     * Метод для получения идентификатора продукта
     */
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

//    /**
//     * Метод для расчета общей суммы строки заказа
//     */
//    public BigDecimal calculateLineTotal() {
//        return price.multiply(quantity);
//    }
}

