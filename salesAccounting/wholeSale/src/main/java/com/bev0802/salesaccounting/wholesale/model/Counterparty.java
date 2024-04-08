package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляет собой модель Контрагента в системе.
 * Содержит основную информацию о Контрагенте, такую как
 */
@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми аргументами.
public class Counterparty {
    private Long id;
    private String name;
    private String inn;
    private String kpp;
    private String address;
    private String email;
    private String password;

    private Organization organization;
}
