package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private LocalDateTime orderDate;

    @ManyToOne
    private Organization buyerOrganization;

    @ManyToOne
    private Organization sellerOrganization;

    @ManyToOne
    private Employee employee;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;
}
