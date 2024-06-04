package com.bev0802.salesaccounting.wholesale.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
@Data // Lombok аннотация для генерации геттеров, сеттеров, toString, equals и hashCode методов.
@NoArgsConstructor // Lombok аннотация для генерации конструктора без аргументов.
@AllArgsConstructor // Lo

public class Order {

    private Long id;
    private Organization buyerOrganization;
    private Organization sellerOrganization;
    private Employee employee;
    private Set<OrderItem> items;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderDate;

    private String orderNumber;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime statusChangeDate;

    private BigDecimal totalAmount;
    private String comments;
}