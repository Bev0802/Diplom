package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
/**
 * Модель сотрудника организации.
 * Содержит информацию о его идентификаторе, имени, должности, электронной почте, пароле,
 * связи с организацией и списке заказов, выполненных сотрудником.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    /**
     * Уникальный идентификатор сотрудника.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Имя сотрудника.
     */
    private String name;
    /**
     * Должность сотрудника.
     */
    private String position;
    /**
     * Электронная почта сотрудника.
     */
    @Column(unique = true)
    private String email;
    /**
     * Пароль сотрудника.
     */
    @Column(nullable = false)
    private String password;
    /**
     * Организация, к которой относится сотрудник.
     */
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    /**
     * Список заказов, выполненных сотрудником.
     */
    @OneToMany(mappedBy = "employee")
    private Set<Order> orders;
}
