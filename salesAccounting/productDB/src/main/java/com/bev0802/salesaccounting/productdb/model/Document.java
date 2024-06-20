package com.bev0802.salesaccounting.productdb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Модель для представления документа в базе данных.
 * Содержит информацию о номере документа, дате создания, организациях-продавце и покупателе,
 * ответственном сотруднике покупателя, списке позиций документа и общей сумме.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Номер документа.
     */
    private String documentNumber;
    /**
     * Дата создания документа. По умолчанию - текущая дата с форматом "dd-MM-yyyy HH:mm:ss".
     */
    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime documentDate = LocalDateTime.now();
    /**
     * Организация-продавец.
     */
    @ManyToOne
    @JoinColumn(name = "seller_organization_id")
    private Organization sellerOrganization;
    /**
     * Организация-покупатель.
     */
    @ManyToOne
    @JoinColumn(name = "buyer_organization_id")
    private Organization buyerOrganization;
    /**
     * Ответственный сотрудник организации-покупателя.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee responsibleEmployeeBuyer;
    /**
     * Список позиций (товаров) в документе.
     */
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
