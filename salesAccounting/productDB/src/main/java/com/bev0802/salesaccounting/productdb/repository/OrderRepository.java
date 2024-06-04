package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Order;
import com.bev0802.salesaccounting.productdb.model.OrderItem;
import com.bev0802.salesaccounting.productdb.model.enumerator.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT MAX(o.orderNumber) FROM Order o WHERE o.orderNumber LIKE :prefix%")
    String findLastOrderNumberByPrefix(@Param("prefix") String prefix);

    default int findNextNumberForPrefix(String prefix) {
        String lastOrderNumber = findLastOrderNumberByPrefix(prefix);
        if (lastOrderNumber != null && !lastOrderNumber.isEmpty()) {
            int lastNumber = Integer.parseInt(lastOrderNumber.split("/")[1]);
            return lastNumber + 1;
        }
        return 1;
    }

    List<Order> findByBuyerOrganizationId(Long buyerId);
    List<Order> findBySellerOrganizationIdAndStatus(Long sellerId, OrderStatus status);

    // Метод для поиска всех заказов, созданных конкретным сотрудником
    List<Order> findByEmployeeId(Long employeeId);

    List<Order> findBySellerOrganizationIdAndStatusNot(Long seller_organization_id, OrderStatus orderStatus);

    @Query("SELECT o.items FROM Order o WHERE o.id = :orderId")
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);


    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items oi LEFT JOIN FETCH oi.product WHERE o.id = :orderId AND o.sellerOrganization.id = :organizationId AND o.employee.id = :employeeId")
    Optional<Order> findByIdAndOrganizationIdAndEmployeeId(@Param("orderId") Long orderId, @Param("organizationId") Long organizationId, @Param("employeeId") Long employeeId);

    List<Order> findByBuyerOrganizationIdAndStatus(Long buyerOrganizationId, OrderStatus orderStatus);

}




