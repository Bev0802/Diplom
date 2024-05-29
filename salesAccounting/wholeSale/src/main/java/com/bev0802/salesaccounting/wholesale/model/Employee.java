package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми аргументами.
public class Employee {

    private Long id;
    private String name;
    private String position;
    private String email;
    private String password;
    private Organization organization;
    private List<Order> orders;
}
