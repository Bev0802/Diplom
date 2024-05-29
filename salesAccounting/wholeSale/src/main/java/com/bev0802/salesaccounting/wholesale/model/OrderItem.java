package com.bev0802.salesaccounting.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Order order;
    private Product product;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;

    /**
     * Метод для получения идентификатора продукта
     */
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    /**
     * Метод для расчета общей суммы строки заказа
     */
    public BigDecimal calculateAmount() {
        return price.multiply(quantity);
    }
}
