package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Класс представляет собой модель Организации в системе.
 * Содержит основную информацию об Организации, такую как его идентификатор, наименование,
 **/

@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми аргументами.
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {

    private Long id;
    @Getter
    private String name;
    private String inn;
    private String kpp;
    private String address;
    private String email;
    private String password;

    private Set<Product> products;
    private Set<Employee> employees;

//    @JsonIgnore
    private List<Order> placedOrders;
    private List<Order> receivedOrders;


    public String getName() {
        return name;
    }
}
