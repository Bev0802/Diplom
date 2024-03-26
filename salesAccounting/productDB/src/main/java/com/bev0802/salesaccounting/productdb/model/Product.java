package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс Product представляет собой модель товара в системе учета товаров.
 * Он содержит информацию о товаре, включая его идентификатор, имя, описание,
 * количество на складе и цену.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    /**
     * Уникальный идентификатор товара.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя товара.
     */
    private String name;

    /**
     * Описание товара.
     */
    private String description;

    /**
     * Количество товара на складе.
     */
    private BigDecimal quantity;

    /**
     * Цена товара.
     */
    private BigDecimal price;
    /**
     * Организация которой принадлежит этот продукт
     */
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}

