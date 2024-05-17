package com.bev0802.salesaccounting.productdb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime documentDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "buyer_organization_id")
    private Organization buyerOrganization;

    @ManyToOne
    @JoinColumn(name = "seller_organization_id")
    private Organization sellerOrganization;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee responsibleEmployee;

    @OneToMany(mappedBy = "document")
    private List<DocumentItem> items;

    private String buyerDocumentNumber;
    private String sellerDocumentNumber;

    public void generateDocumentNumbers() {
        // Предположим, что номера документов генерируются на основе ID документа и даты
        this.buyerDocumentNumber = "B-" + this.id + "-" + documentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.sellerDocumentNumber = "S-" + this.id + "-" + documentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
