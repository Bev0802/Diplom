package com.bev0802.salesaccounting.productdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Set;
/**
 * Модель организации.
 * Содержит информацию об идентификаторе, названии, ИНН, КПП, адресе, электронной почте, пароле,
 * списке продуктов, сотрудников, размещенных и полученных заказов.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Наименование организации.
     */
    private String name;
    /**
     * ИНН организации.
     */
    private String inn;
    /**
     * КПП организации.
     */
    private String kpp;

    /**
     * Юридический адрес организации.
     */
    private String address;
    /**
     * Электронная почта организации
     */
    private String email;
    /**
     * Пароль организации
     */
    private String password;
    /**
     * Список продуктов, принадлежащих организации.
     */
    @OneToMany(mappedBy = "organization")
    private Set<Product> products;
    /**
     * Список сотрудников, работающих в организации.
     */
    @OneToMany(mappedBy = "organization")
    private Set<Employee> employees;
    /**
     * Список заказов, размещенных этой организацией как покупателем.
     */
    @OneToMany(mappedBy = "buyerOrganization")
    private Set<Order> placedOrders;
    /**
     * Список заказов, полученных этой организацией как продавцом.
     */
    @OneToMany(mappedBy = "sellerOrganization")
    private Set<Order> receivedOrders;
}