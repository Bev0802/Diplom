package com.bev0802.salesaccounting.productdb.repository.specification;

import com.bev0802.salesaccounting.productdb.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Класс, предоставляющий спецификации для {@link Product}, используемые для создания критериев запросов к базе данных.
 * Спецификации могут быть использованы для построения сложных условий поиска с использованием критериев API JPA.
 */
public class ProductSpecifications {

    /**
     * Создаёт спецификацию для поиска продуктов, имя которых содержит указанную подстроку, не учитывая регистр.
     *
     * @param name Подстрока, которую должно содержать имя продукта.
     * @return Спецификация для использования в запросе.
     */
    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Создаёт спецификацию для поиска продуктов, количество которых больше нуля.
     *
     * @return Спецификация для использования в запросе.
     */
    public static Specification<Product> quantityGreaterThanZero() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("quantity"), BigDecimal.ZERO);
    }

    /**
     * Создаёт спецификацию для поиска продуктов с ценой в заданном диапазоне.
     *
     * @param startPrice Нижняя граница диапазона цен.
     * @param endPrice Верхняя граница диапазона цен.
     * @return Спецификация для использования в запросе.
     */
    public static Specification<Product> priceBetween(BigDecimal startPrice, BigDecimal endPrice) {
        return (root, query, criteriaBuilder) -> {
            if (startPrice == null || endPrice == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("price"), startPrice, endPrice);
        };
    }

    /**
     * Создаёт спецификацию для поиска продуктов, количество которых равно нулю.
     *
     * @return Спецификация для использования в запросе.
     */
    public static Specification<Product> quantityEqualToZero() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("quantity"), BigDecimal.ZERO);
    }
}


