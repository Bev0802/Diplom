package com.bev0802.salesaccounting.wholesale.controller;


import com.bev0802.salesaccounting.wholesale.model.Order;
import com.bev0802.salesaccounting.wholesale.model.OrderItem;
import com.bev0802.salesaccounting.wholesale.service.OrderService;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import com.bev0802.salesaccounting.wholesale.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("organization/{organizationId}/employee/{employeeId}/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProductService productService;
    @Autowired EmployeeController employeeController;
    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;   }


    @PostMapping("/newOrder")
    public Order createOrder(@RequestBody Order order,
                             @RequestParam Long organizationId,
                             @RequestParam Long employeeId) {
        logger.info("Creating new order {} for organization {} and employee {}", order, organizationId, employeeId);
        return orderService.createOrder(order, organizationId, employeeId);
    }

    @PostMapping("/addProduct")
    public void addProductToOrder(@RequestParam Long organizationId,
                                  @RequestParam Long employeeId,
                                  @RequestParam Long orderId,
                                  @RequestParam Long productId,
                                  @RequestParam int quantity) {
        logger.info("Creating new product{} in order{} for organization{} and employee{}", productId, orderId, organizationId, employeeId);
        orderService.addProductToOrder(organizationId, employeeId, orderId, productId, quantity);
    }


//#region getMapping
    /**
     * Получение списка заказов по ID продавца, исключая новые.
     * @param organizationId ID продавца.
     * @param employeeId ID сотрудника.
     * @return Список заказов.
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
        if (source != null) {
            model.addAttribute("returnUrl", source);
        } else {
            model.addAttribute("returnUrl", "/organization/" + organizationId + "/employee/" + employeeId + "/orders/sellerList");
        }
            // Возвращение имени шаблона Thymeleaf для отображения списка заказов
            return "listOrders";
    }

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
        if (source != null) {
            model.addAttribute("returnUrl", source);
        } else {
            model.addAttribute("returnUrl", "/organization/" + organizationId + "/employee/" + employeeId + "/orders/buyerList");
        }
        // Возвращение имени шаблона Thymeleaf для отображения списка заказов
        return "listOrders";
    }

    /**
     * Форма заказа с деталями
     * @param organizationId ID организации
     * @param employeeId ID сотрудника
     * @param orderId ID заказа
     * @param model объект модели
     * @return имя шаблона Thymeleaf
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
        List<OrderItem> items = orderService.getOrderItems(organizationId, orderId);
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

    @GetMapping("/employeeOrders")
    public List<Order> getOrdersByEmployee(@RequestParam Long organizationId,
                                           @RequestParam Long employeeId) {
        logger.info("Getting orders for organization {} and employee {}", organizationId, employeeId);
        return orderService.getOrdersByEmployee(organizationId, employeeId);
    }
    //#endregion

//#region изменение статусов
    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable("organizationId") Long organizationId,
                              @PathVariable("employeeId") Long employeeId,
                              @PathVariable("orderId") Long orderId,
                              HttpServletRequest request) {
        orderService.cancelOrder(orderId, organizationId, employeeId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


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

    @PostMapping("/ship/{orderId}")
    public String shipOrder(@PathVariable("organizationId") Long organizationId,
                           @PathVariable("employeeId") Long employeeId,
                           @PathVariable("orderId") Long orderId,
                           HttpServletRequest request){
        logger.info("Shipping order {} for organization {}", orderId, organizationId);
        orderService.shipOrder(orderId, organizationId, employeeId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    //#endregion

}
