package com.bev0802.salesaccounting.wholesale.controller;


import com.bev0802.salesaccounting.wholesale.model.Order;
import com.bev0802.salesaccounting.wholesale.model.OrderItem;
import com.bev0802.salesaccounting.wholesale.service.OrderService;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("organization/{organizationId}/employee/{employeeId}/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrganizationService organizationService;

    //#region Создание и добавление товаров в заказ

    /**
     * Создает новый заказ.
     *
     * @param order Заказ, который нужно создать.
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param productId ID товара.
     * @param quantity Количество товара.
     * @return Созданный заказ.
     */
    @PostMapping("/newOrder")
    public Order createOrder(@RequestBody Order order,
                             @RequestParam Long organizationId,
                             @RequestParam Long employeeId,
                             @RequestParam Long productId,
                             @RequestParam BigDecimal quantity) {
        logger.info("Creating new order {} for organization {} and employee {}", order, organizationId, employeeId);
        return orderService.createOrder(order, organizationId, employeeId, productId, quantity);
    }
    /**
     * Добавляет продукт в заказ.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param productId ID товара.
     * @param quantity Количество товара.
     * @return Редирект на страницу доступных для покупки товаров.
     */
    @PostMapping("/addProductToOrder")
    public String addProductToOrder(@PathVariable("organizationId") Long organizationId,
                                    @PathVariable("employeeId") Long employeeId,
                                    @RequestParam Long productId,
                                    @RequestParam BigDecimal quantity) {
        logger.info("Adding product {} with quantity {} to order for organization {} and employee {}", productId, quantity, organizationId, employeeId);
        orderService.addProductToOrder(organizationId, employeeId, productId, quantity);
        return "redirect:/organization/" + organizationId + "/employee/" + employeeId + "/product/availableForPurchase";
    }
//#endregion

    //#region Получение заказов

//#region getMapping
    /**
     * Получение списка заказов по ID продавца, исключая новые.
     *
     * @param organizationId ID продавца.
     * @param employeeId ID сотрудника.
     * @param source Источник запроса (опционально).
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона Thymeleaf для отображения списка заказов.
     */
    @GetMapping("/sellerList")
    public String getOrdersBySeller(@PathVariable Long organizationId,
                                    @PathVariable Long employeeId,
                                    @RequestParam(required = false) String source,
                                    Model model) {
            // Получение списка заказов, исключая новые, по идентификатору организации продавца и сотрудника
            List<Order> orders = orderService.findOrdersBySellerIdExcludingNew(organizationId, employeeId);
            // Перевод статусов на русский
            orders = orderService.translateOrderStatuses(orders);
            // Добавление данных в модель для отображения в шаблоне
            model.addAttribute("orders", orders);
            model.addAttribute("organizationId", organizationId);
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
            logger.info("Orders for organization {} and employee {} retrieved: {}", organizationId, employeeId, orders);

        // Сохранить путь возвращения в модель
        model.addAttribute("returnUrl", Objects.requireNonNullElseGet(source, () -> "/organization/" + organizationId + "/employee/" + employeeId + "/orders/sellerList"));
            // Возвращение имени шаблона Thymeleaf для отображения списка заказов
            return "listOrders";
    }
    /**
     * Получение списка заказов по ID покупателя, исключая новые.
     *
     * @param organizationId ID покупателя.
     * @param employeeId ID сотрудника.
     * @param source Источник запроса (опционально).
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона Thymeleaf для отображения списка заказов.
     */
    @GetMapping("/buyerList")
    public String getOrdersByBuyer(@PathVariable Long organizationId,
                                   @PathVariable Long employeeId,
                                   @RequestParam(required = false) String source,
                                    Model model) {
        // Получение списка заказов, исключая новые, по идентификатору организации продавца и сотрудника
        List<Order> orders = orderService.findOrdersByBuyerIdExcludingNew(organizationId, employeeId);
        // Перевод статусов на русский
        orders = orderService.translateOrderStatuses(orders);
        // Добавление данных в модель для отображения в шаблоне
        model.addAttribute("orders", orders);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        model.addAttribute("isSeller", false); // Организация не является продавцом
        model.addAttribute("isActive", true); // Можно определить активность кнопок в зависимости от нужд
        logger.info("Orders for organization {} and employee {} retrieved: {}", organizationId, employeeId, orders);

        // Сохранить путь возвращения в модель
        model.addAttribute("returnUrl", Objects.requireNonNullElseGet(source, () -> "/organization/" + organizationId + "/employee/" + employeeId + "/orders/buyerList"));
        // Возвращение имени шаблона Thymeleaf для отображения списка заказов
        return "listOrders";
    }
    /**
     * Получение списка новых заказов.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param source Источник запроса (опционально).
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона Thymeleaf для отображения списка заказов.
     */
    @GetMapping("/newList")
    public String getNewOrders(@PathVariable Long organizationId,
                               @PathVariable Long employeeId,
                               @RequestParam(required = false) String source,
                               Model model) {
        // Получение списка заказов, исключая новые, по идентификатору организации продавца и сотрудника
        List<Order> orders = orderService.findNewOrdersByBuyerId(organizationId, employeeId);
        orders.sort(Comparator.comparing(Order::getId).reversed());
        // Перевод статусов на русский
        orders = orderService.translateOrderStatuses(orders);
        /* Добавление данных в модель для отображения в шаблоне */
        model.addAttribute("orders", orders);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organizationService.getOrganizationName(organizationId));
        model.addAttribute("isSeller", false); // Организация не является продавцом
        model.addAttribute("isActive", true); // Можно определить активность кнопок в зависимости от нужд
        logger.info("Orders for organization {} and employee {} retrieved: {}", organizationId, employeeId, orders);
        return "listOrders";
    }

    /**
     * Получение деталей заказа.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param orderId ID заказа.
     * @param model Модель для передачи данных в представление.
     * @param request HTTP запрос.
     * @param returnUrl URL для возврата.
     * @param session HTTP сессия.
     * @return Имя шаблона Thymeleaf для отображения деталей заказа.
     */
    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable Long organizationId,
                                  @PathVariable Long employeeId,
                                  @PathVariable Long orderId,
                                  Model model,
                                  HttpServletRequest request,
                                  @RequestParam(required = false) String returnUrl,
                                  HttpSession session) {
        // Получаем заказ по его ID и данным организации и сотрудника
        Order order = orderService.findOrderById(orderId, organizationId, employeeId);
        // Получаем название организации
        String organizationName = organizationService.findById(organizationId).getName();
        // Переводим статусы заказа на русский
        order = orderService.translateOrderStatuses(order);

        // Получаем список товаров в заказе
        //List<OrderItem> items = orderService.getOrderItems(organizationId, orderId);
        //order.setItems(new HashSet<>(items));

        // Получаем список товаров в заказе и сортируем по ID
        List<OrderItem> items = new ArrayList<>(orderService.getOrderItems(organizationId, orderId));
        items.sort(Comparator.comparing(OrderItem::getId).reversed()); // Сортировка по ID в обратном порядке
        order.setItems(new HashSet<>(items));

        // Получаем ID организации продавца из заказа
        Long sellerOrganizationId = order.getSellerOrganization().getId();
        // Проверяем, является ли текущая организация продавцом или покупателем
        boolean isSeller = organizationId.equals(sellerOrganizationId);
        // Определяем, какие кнопки должны быть активными
        boolean isActiveSeller = isSeller;
        boolean isActiveBuyer = !isSeller;


        // Передаем данные в модель для отображения на странице
        model.addAttribute("order", order);
        model.addAttribute("organizationName", organizationName);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("items", items);
        model.addAttribute("isActiveSeller", isActiveSeller); // Флаг, указывающий, нужно ли активировать кнопки для текущей организации
        model.addAttribute("isActiveBuyer", isActiveBuyer); // Флаг, указывающий, нужно ли активировать кнопки для текущей организации
        model.addAttribute("httpServletRequest", request);


        // Логируем информацию о полученных товарах
        logger.info("Items for order {} of organization {} and employee {} retrieved: {}", orderId, organizationId, employeeId, order, items);

        // Сохранить returnUrl в сессии
        if (returnUrl != null) {
            session.setAttribute("returnUrl", returnUrl);
        }
        return "detailOrder"; // Возвращаем имя шаблона Thymeleaf для отображения страницы

    }
    /**
     * Получение списка заказов для сотрудника.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @return Список заказов.
     */
    @GetMapping("/employeeOrders")
    public List<Order> getOrdersByEmployee(@RequestParam Long organizationId,
                                           @RequestParam Long employeeId) {
        logger.info("Getting orders for organization {} and employee {}", organizationId, employeeId);
        return orderService.getOrdersByEmployee(organizationId, employeeId);
    }
    //#endregion

//#region изменение статусов
    /**
     * Отмена заказа.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param orderId ID заказа.
     * @param request HTTP запрос.
     * @return Редирект на предыдущую страницу.
     */
    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable("organizationId") Long organizationId,
                              @PathVariable("employeeId") Long employeeId,
                              @PathVariable("orderId") Long orderId,
                              HttpServletRequest request) {
        orderService.cancelOrder(orderId, organizationId, employeeId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Подтверждение заказа.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param orderId ID заказа.
     * @param request HTTP запрос.
     * @return Редирект на предыдущую страницу.
     */
    @PostMapping("/confirm/{orderId}")
    public String confirmOrder(@PathVariable("organizationId") Long organizationId,
                              @PathVariable("employeeId") Long employeeId,
                              @PathVariable("orderId") Long orderId,
                              HttpServletRequest request) {
        logger.info("Confirming order {} for organization {}", orderId, organizationId);
        orderService.confirmOrder(orderId, organizationId, employeeId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    /**
     * Оплата заказа.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param orderId ID заказа.
     * @param request HTTP запрос.
     * @return Редирект на предыдущую страницу.
     */
    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable("organizationId") Long organizationId,
                          @PathVariable("employeeId") Long employeeId,
                          @PathVariable("orderId") Long orderId,
                          HttpServletRequest request){
        logger.info("Paying order {} for organization {}", orderId, organizationId);
        orderService.payOrder(orderId, organizationId, employeeId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    /**
     * Отгрузка заказа.
     *
     * @param organizationId ID организации.
     * @param employeeId ID сотрудника.
     * @param orderId ID заказа.
     * @param request HTTP запрос.
     * @param redirectAttributes Атрибуты для редиректа.
     * @return Редирект на предыдущую страницу или на страницу ошибки в случае ошибки.
     */
    @PostMapping("/ship/{orderId}")
    public String shipOrder(@PathVariable("organizationId") Long organizationId,
                            @PathVariable("employeeId") Long employeeId,
                            @PathVariable("orderId") Long orderId,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        logger.info("Shipping order {} for organization {}", orderId, organizationId);
        String errorMessage = orderService.shipOrder(orderId, organizationId, employeeId);

        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/error";
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    //#endregion
}
