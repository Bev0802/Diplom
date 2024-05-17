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

    List<Order> findBySellerOrganizationIdAndStatusNot(Long sellerId, OrderStatus orderStatus);

    @Query("SELECT o.items FROM Order o WHERE o.id = :orderId")
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);

    // Метод для поиска активных заказов по статусу и ID организации
    List<Order> findByStatusAndBuyerOrganizationId(OrderStatus status, Long organizationId);

    // Альтернативно, если вы хотите искать заказы и по продавцу, вы можете использовать:
    List<Order> findByStatusAndSellerOrganizationId(OrderStatus status, Long organizationId);

    // Если необходимо искать по обоим полям одновременно
    List<Order> findByStatusAndBuyerOrganizationIdOrSellerOrganizationId(OrderStatus status, Long buyerOrgId, Long sellerOrgId);
}

