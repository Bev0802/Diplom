package com.bev0802.salesaccounting.productdb.model;

import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    /**
     * Уникальный идентификатор заказа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Организация, которая совершает покупку.
     */
    @ManyToOne
    @JoinColumn(name = "buyer_organization_id")
    private Organization buyerOrganization;

    /**
     * Организация, которая продает товары.
     */
    @ManyToOne
    @JoinColumn(name = "seller_organization_id")
    private Organization sellerOrganization;

    /**
     * Сотрудник, оформивший заказ.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /**
     * Набор товарных позиций, включенных в заказ.
     */
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<OrderItem> items;


    /**
     * Статус заказа, определенный перечислением {@link OrderStatus}.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Дата и время создания заказа, автоматически устанавливаемые при создании объекта.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderDate = LocalDateTime.now();

    /**
     * Уникальный номер заказа, используемый для идентификации в системе.
     */
    private String orderNumber;

    /**
     * Дата и время изменения статуса заказа, автоматически устанавливаемые при изменении статуса.
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime statusChangeDate;

    /**
     * Общая сумма заказа в денежном выражении.
     */
    private BigDecimal totalAmount;

    /**
     * Дополнительные комментарии к заказу, могут включать специальные инструкции или заметки.
     */
    @Column
    private String comments;

    @JoinColumn(name = "document_id")
    private Long document;

    @ElementCollection
    private Map<Long, BigDecimal> reservedProducts = new HashMap<>();
}
