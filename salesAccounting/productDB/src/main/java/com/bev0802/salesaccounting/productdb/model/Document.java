package com.bev0802.salesaccounting.productdb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentNumber;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime documentDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "seller_organization_id")
    private Organization sellerOrganization;

    @ManyToOne
    @JoinColumn(name = "buyer_organization_id")
    private Organization buyerOrganization;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee responsibleEmployeeBuyer;

    @OneToMany(mappedBy = "documentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentItem> items;
    /**
     * Общая сумма заказа в денежном выражении.
     */
    private BigDecimal totalAmount;
    /**
     * Идентификатор заказа на основании, которого создали документ
     */
    private Long orderId;
}
