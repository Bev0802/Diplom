package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Класс представляет собой модель Организации в системе.
 * Содержит основную информацию об Организации, такую как его идентификатор, наименование,
 **/

@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми аргументами.
public class Organization {

    private Long id;
    private String name;
    private String inn;
    private String kpp;
    private String address;
    private String email;
    private String password;

    private Set<Product> products;
    private Set<Employee> employees;
    private Set<Counterparty> counterparties;

}
