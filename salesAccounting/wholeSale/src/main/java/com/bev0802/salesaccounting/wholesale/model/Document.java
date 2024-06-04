package com.bev0802.salesaccounting.wholesale.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private Long id;

    private String documentNumber;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime documentDate;
    private Organization sellerOrganization;
    private Organization buyerOrganization;
    private Employee responsibleEmployeeBuyer;
    private List<DocumentItem> items;
    private BigDecimal totalAmount;
    private Long orderId;
}
