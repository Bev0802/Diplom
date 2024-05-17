package com.bev0802.salesaccounting.productdb.repository.specification;

import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.model.OrderItem;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {

    /**
     * Спецификация для фильтрации заказов по идентификатору организации.
     */
    public static Specification<Order> hasOrganization(Long organizationId) {
        return (root, query, cb) -> {
            Predicate buyer = cb.equal(root.get("buyerOrganization").get("id"), organizationId);
            Predicate seller = cb.equal(root.get("sellerOrganization").get("id"), organizationId);
            return cb.or(buyer, seller);
        };
    }

    /**
     * Спецификация для фильтрации заказов по наличию продукта.
     */
    public static Specification<Order> hasProduct(Long productId) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<OrderItem> orderItem = subquery.from(OrderItem.class);
            subquery.select(orderItem.get("order").get("id"));
            subquery.where(cb.equal(orderItem.get("product").get("id"), productId));

            return cb.in(root.get("id")).value(subquery);
        };
    }

    /**
     * Спецификация для исключения заказов с статусом NEW.
     */
    public static Specification<Order> isNotNew() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), OrderStatus.NEW);
    }

    /**
     * Спецификация для фильтрации заказов по продавцу, исключая заказы с статусом NEW.
     */
    public static Specification<Order> bySellerExcludingNew(Long sellerId) {
        return Specification.where(hasOrganization(sellerId)).and(isNotNew());
    }

    public static Specification<Order> hasBuyerOrganization(Long buyerOrganizationId) {
        return (root, query, cb) -> cb.equal(root.get("buyerOrganization").get("id"), buyerOrganizationId);
    }

    public static Specification<Order> hasSellerOrganization(Long sellerOrganizationId) {
        return (root, query, cb) -> cb.equal(root.get("sellerOrganization").get("id"), sellerOrganizationId);

    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Order> isWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> cb.between(root.get("orderDate"), startDate, endDate);
    }

    public static Specification<Order> bySeller(Long sellerId) {
        return (root, query, cb) -> cb.equal(root.get("sellerOrganization").get("id"), sellerId);
    }

    /**
     * Собирает все спецификации в одну для фильтрации заказов по нескольким критериям.
     */
    public static Specification<Order> filterByCriteria(Long sellerId, Long buyerOrganizationId, OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Order> spec = Specification.where(bySeller(sellerId));

        if (buyerOrganizationId != null) {
            spec = spec.and(hasBuyerOrganization(buyerOrganizationId));
        }
        if (status != null) {
            spec = spec.and(hasStatus(status));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(isWithinDateRange(startDate, endDate));
        }

        return spec;
    }
}