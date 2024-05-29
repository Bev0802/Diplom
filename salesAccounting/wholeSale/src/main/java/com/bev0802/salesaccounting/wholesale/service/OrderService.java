package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Order;
import com.bev0802.salesaccounting.wholesale.model.OrderItem;
import com.bev0802.salesaccounting.wholesale.model.enumerator.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* Сервис для работы с заказами
 */
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Словарь для перевода статусов в строковые значения
     */
    private static final Map<String, String> STATUS_TRANSLATIONS = Map.of(
            "NEW", "Новый",
            "CONFIRMED", "Подтвержден",
            "PAID", "Оплачен",
            "SHIPPED", "Отгружен/завершен",
            "CANCELLED", "Отменен"
    );

    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    public OrderService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${productDB.service.url}")
    private String productDBServiceUrl;

    /**
     * Создание нового заказа
     *
     * @param order          заказ
     * @param organizationId идентификатор организации
     * @param employeeId     идентификатор сотрудника
     * @return созданный заказ
     */
    public Order createOrder(Order order, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + organizationId + "/employee/" + employeeId + "/orders/newOrder";
        logger.info("URL: " + url);
        return restTemplate.postForObject(url, order, Order.class);
    }

    /**
     * Добавление продукта в заказ
     *
     * @param organizationId идентификатор организации
     * @param employeeId     идентификатор сотрудника
     * @param orderId        идентификатор заказа
     * @param productId      идентификатор продукта
     * @param quantity       количество
     */
    public void addProductToOrder(Long organizationId, Long employeeId, Long orderId, Long productId, int quantity) {
        String url = productDBServiceUrl + organizationId + "/employee/" + employeeId + "/orders/addProduct/" + orderId;
        logger.info("URL: " + url);
        restTemplate.postForObject(url, null, Void.class, productId, quantity);
    }



    /**
     * Перевод статусов в строковые значения
     *
     * @param orders Список заказов
     * @return Список заказов с переведенными статусами
     */
    public List<Order> translateOrderStatuses(List<Order> orders) {
        for (Order order : orders) {
            order.setStatus(STATUS_TRANSLATIONS.getOrDefault(order.getStatus(), order.getStatus()));
        }
        return orders;
    }
    public Order translateOrderStatuses(Order order) {
        order.setStatus(STATUS_TRANSLATIONS.getOrDefault(order.getStatus(), order.getStatus()));
        return order;
    }
//todo: Допиши методы
    /**
     * Удаление товара из заказа
     * @param orderId
     * @param orderItemId
     * @return
     */

    /**
     * Изменение количества продукта в заказе
     * @param orderId
     * @param orderItemId
     * @param quantity
     * @return
     */

//#region Изменение статусов
    /**
     * Подтверждение заказа
     *
     * @param organizationId идентификатор организации
     * @param orderId        идентификатор заказа
     * @return заказ c измененным статусом на confirm
     */
    public void confirmOrder(Long orderId, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/confirm/" + orderId;
        logger.info("URL: " + url);
        restTemplate.postForObject(url, null, Order.class);
    }

    /**
     * Оплата заказа
     *
     * @param organizationId идентификатор организации
     * @param orderId        идентификатор заказа
     * @return заказ c измененным статусом на pay
     */

    public void payOrder(Long organizationId, Long orderId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/pay/" + orderId;
        logger.info("URL: " + url);
        restTemplate.postForObject(url, null, Order.class);
    }

    /**
     * Отправка заказа
     *
     * @param organizationId идентификатор организации
     * @param orderId        идентификатор заказа
     * @return заказ c измененным статусом на shipped
     */
    public void shipOrder(Long orderId, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/ships/" + orderId;
        logger.info("URL: " + url);
        restTemplate.postForObject(url, null, Order.class);
    }
    public void cancelOrder(Long orderId, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/cancel/" + orderId;
        logger.info("URL: " + url);
        restTemplate.postForObject(url, null, String.class);
    }

//#endregion
//#region get запросы
    /**
 * Получение заказа по идентификатору
 * @param orderId
 * @return Заказ
 */
    public Order findOrderById(Long orderId, Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/" + orderId;
        logger.info("URL: " + url);
        return restTemplate.getForObject(url, Order.class);
    } /**
     * Получение списка товарных позиций по идентификатору заказа.
     * @param orderId ID заказа.
     * @return Список товарных позиций в заказе.
     */
    public List<OrderItem> getOrderItems(Long organizationId, Long orderId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + orderId + "/orders/" + orderId + "/items";
        logger.info("URL: " + url);
        OrderItem[] items = restTemplate.getForObject(url, OrderItem[].class);
        assert items != null;
        return List.of(items);
    }
        /**
         * Получение списка заказов сотрудника
         *
         * @param organizationId идентификатор организации
         * @param employeeId     идентификатор сотрудника
         * @return список заказов
         */
    public List<Order> getOrdersByEmployee(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/employeeOrders";
        logger.info("URL: " + url);
        return restTemplate.getForObject(url, List.class);
    }

    /**
     * Получение списка заказов(покупок) по ID органиазции которая вошла в систему.
     * @param buyerId ID покупателя.
     * @return Список заказов покупателя.
     */
    public List<Order> getOrdersByBuyer(Long organizationId, Long buyerId) {
            String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + buyerId + "/orders/buyerList";
            logger.info("URL: " + url);
            Order[] orders = restTemplate.getForObject(url, Order[].class);
            assert orders != null;
            return List.of(orders);
    }

    /**
     * Получение списка заказов(продаж) по ID продавца, исключая новые.
     * @param organizationId ID продавца.
     * @return Список заказов.
     * */

    public List<Order> findOrdersBySellerIdExcludingNew(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/sellerList";
        logger.info("URL: {}", url);

        try {
            Order[] orders = restTemplate.getForObject(url, Order[].class);
            return Arrays.asList(orders);
        } catch (RestClientException e) {
            logger.error("Error while extracting response from productDB service", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while parsing JSON response from productDB service", e);
            throw new RuntimeException("Error while parsing JSON response", e);
        }
    }
    public List<Order> findOrdersByBuyerIdExcludingNew(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId + "/orders/buyerList";
        logger.info("URL: {}", url);

        try {
            Order[] orders = restTemplate.getForObject(url, Order[].class);
            return Arrays.asList(orders);
        } catch (RestClientException e) {
            logger.error("Error while extracting response from productDB service", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while parsing JSON response from productDB service", e);
            throw new RuntimeException("Error while parsing JSON response", e);
        }
    }

    /**
     * Получение списка товарных позиций по идентификатору заказа.
     * @param orderId ID заказа.
     * @return Список товарных позиций в заказе.
     */
    // Проверяем наличие метода getOrderItems

    /**
     * Получение списка заказов по фильтрам.
     * @param sellerId Продавец.
     * @param buyerId Покупатель.
     * @param status Статус заказа.
     * @param startDate Начальная дата.
     * @return Отфильтрованный список заказов.
     */
    public List<Order> getFilteredOrders(Long organizationId, Long sellerId, Long buyerId,
                                         OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
            // Формирование URL с учетом фильтров
            String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/orders/filter";
            if (sellerId != null) {
                url += "?sellerId=" + sellerId;
            }
            if (buyerId != null) {
                url += "&buyerId=" + buyerId;
            }
            if (status != null) {
                url += "&status=" + status.name();
            }
            if (startDate != null) {
                url += "&startDate=" + startDate;
            }
            if (endDate != null) {
                url += "&endDate=" + endDate;
            }
            Order[] orders = restTemplate.getForObject(url, Order[].class);
            assert orders != null;
            return List.of(orders);
   }
//#endregion

}