package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentItem {

    private Long id;
    private Long documentId;
    private Product product;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
}

