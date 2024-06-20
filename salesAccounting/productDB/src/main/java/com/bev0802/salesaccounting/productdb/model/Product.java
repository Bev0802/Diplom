package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название товара. Не может быть пусто
     */
    private String name;
    /**
     * Описание товара
     */
    private String description;
    /**
     * Колличество товара на складе
     */
    private BigDecimal quantity;
    /**
     * Зарезервированное колличество на складе
     */
    private BigDecimal reserved;
    /**
     * Цена за единицу
     */
    private BigDecimal price;
    /**
     * путь к изображению
     */
    private String imageUrl;

    /**
     * -- GETTER --
     *  Возвращает Организацию, к которой принадлежит данный продукт.
     *
     * @return Организация, к которой принадлежит данный продукт.
     */
    @Getter
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    /**
     * Список элементов заказов, в которых упоминается данный продукт.
     */
    @OneToMany(mappedBy = "product")
    private Set<OrderItem> orderItems;

    /**
     * Резервирует указанное количество товара, если оно доступно на складе.
     * Уменьшает количество доступного товара и увеличивает зарезервированное количество.
     *
     * @param quantityToReserve Количество товара, которое необходимо резервировать.
     * @throws IllegalArgumentException если запрашиваемое количество превышает доступное количество товара на складе.
     */
    public void reserveQuantity(BigDecimal quantityToReserve) {
        if (this.quantity.compareTo(quantityToReserve) >= 0) {
            this.quantity = this.quantity.subtract(quantityToReserve);
            this.reserved = this.reserved.add(quantityToReserve);
        } else {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }
    }
    /**
     * Возвращает ранее зарезервированное количество товара на склад.
     * Увеличивает количество доступного товара и уменьшает зарезервированное количество.
     *
     * @param quantityToReturn Количество товара, которое необходимо вернуть на склад.
     */
    public void returnQuantity(BigDecimal quantityToReturn) {
        this.quantity = this.quantity.add(quantityToReturn);
        this.reserved = this.reserved.subtract(quantityToReturn);
    }
}




