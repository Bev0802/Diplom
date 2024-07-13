package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;
import java.math.BigDecimal;
/**
 * Класс представляет собой модель продукта в системе.
 * Содержит основную информацию о продукте, такую как его идентификатор, наименование, описание, количество и цену.
 */
@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми аргументами.
public class Product {
    /**
     * Уникальный идентификатор продукта.
     */
    private Long id;
    /**
     * Наименование продукта.
     */
    private String name;
    /**
     * Описание продукта.
     */
    private String description;
    /**
     * Количество продукта в наличии.
     */
    private BigDecimal quantity;
    /**
     * Цена продукта.
     */
    private BigDecimal price;

    /**
     * Зарезервированное количество продукта.
     */
    private BigDecimal reserved;

    /**
     * Орагниазация, к которой принадлежит продукт.
     */
    private Organization organization;


    public SSLSession getOrganization() {

        return null;
    }

    public String getOrganizationName() {
        return organization.getName();
    }
}


