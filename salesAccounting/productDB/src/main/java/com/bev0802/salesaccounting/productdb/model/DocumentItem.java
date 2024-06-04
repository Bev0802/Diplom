package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "document_id")
    private Long documentId;

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
}
