package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Employee;
import com.bev0802.salesaccounting.wholesale.model.Organization;
import com.bev0802.salesaccounting.wholesale.model.Product;
import com.bev0802.salesaccounting.wholesale.service.EmployeeService;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import com.bev0802.salesaccounting.wholesale.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Контроллер для управления товарами в приложении.
 * Обрабатывает веб-запросы, связанные с продуктами, и взаимодействует с сервисом {@link ProductService} для выполнения бизнес-логики.
 */
@Controller
@RequestMapping("/organization/{organizationId}/employee/{employeeId}/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrganizationService organizationService;

    /**
     * Отображает список всех продуктов.
     *
     * @param model модель для передачи данных в представление.
     * @return имя HTML шаблона для отображения списка продуктов.
     */
    @GetMapping("/")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "listProducts"; // Имя HTML шаблона для отображения списка продуктов
    }

    /**
     * Метод для получения списка товаров, принадлежащих определенной организации.
     * @param organizationId ID организации
     * @param model модель для передачи данных в представление
     * @return имя HTML шаблона для отображения списка товаров
     */
    @GetMapping("/byOrganization/")
        public String getProductsByOrganization (@PathVariable("organizationId") Long organizationId,
                                                 @PathVariable("employeeId") Long employeeId,
                                                 Model model) {
            List<Product> products = productService.findProductsByOrganization(organizationId, employeeId);
            model.addAttribute("products", products);
            model.addAttribute("organizationId", organizationId);
            return "listProducts";
        }

    /**
     * Получает список товаров, доступных для покупки в определенной организации.
     * @param organizationId идентификатор организации
     * @param employeeId идентификатор сотрудника
     * @return возвращает список доступных для покупки
     */
    @GetMapping("/availableForPurchase")
    public String productsAvailableForPurchase(@PathVariable("organizationId") Long organizationId,
                                               @PathVariable("employeeId") Long employeeId,
                                               HttpSession session,
                                               Model model) {
        List<Product> products = productService.findProductsNotBelongingToOrganization(organizationId, employeeId);

        Organization organization = organizationService.findById(organizationId);
        Employee employee = employeeService.getEmployeeById(employeeId, organizationId);

        logger.info("Products available for purchase: {}", products);
        logger.info("Products available for purchase size: {}", products.size());
        logger.info("Organization ID: {}", organizationId);
        logger.info("Organization Name: {}", organization.getName());

        model.addAttribute("products", products);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("organizationName", organization.getName());

        return "market"; // Имя шаблона для отображения списка товаров
    }


    /**
     * Отображает страницу для создания нового продукта.
     * @param organizationId Идентификатор организации, к которой принадлежит продукт.
     * @param employeeId Идентификатор сотрудника, создающего продукт.
     * @param model Модель Spring MVC для передачи данных в представление.
     * @return Название HTML-шаблона для отображения формы деталей продукта.
     */
    @GetMapping("/new")
    public String newProduct(@PathVariable Long organizationId,
                             @PathVariable Long employeeId,
                             Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        return "detailProduct";
    }
    /**
     * Поиск продуктов по наименованию.
     *
     * @param name имя продукта для поиска.
     * @param model модель для передачи данных в представление.
     * @return имя HTML шаблона для отображения списка продуктов.
     */
    @GetMapping("/search")
    public String findProductsByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("products", productService.findProductsByName(name));
        return "listProducts"; // Используйте тот же шаблон, что и для списка всех продуктов
    }
    /**
     * Множественный фильтр для продуктов.
     *
     * @param name имя продукта для поиска.
     * @param available флаг наличия продукта.
     * @param startPrice начальная цена для фильтрации.
     * @param endPrice конечная цена для фильтрации.
     * @param model модель для передачи данных в представление.
     * @return имя HTML шаблона для отображения списка продуктов.
     */
    // Множественный фильтр для продуктов
    @GetMapping("/filteredSearch")
    public String filteredSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "available", required = false) Boolean available,
            @RequestParam(value = "startPrice", required = false) BigDecimal startPrice,
            @RequestParam(value = "endPrice", required = false) BigDecimal endPrice,
            @RequestParam(value = "templateName", required = false, defaultValue = "listProducts") String templateName,
            Model model) {
        List<Product> products = productService.findProductsFiltered(name, available, startPrice, endPrice);

        model.addAttribute("products", products);

        // Дополнительная мета-информация может быть добавлена здесь
        return templateName; // Использует имя шаблона, указанное в параметрах запроса или значение по умолчанию
    }

    /**
     * Отображает страницу с деталями продукта, позволяя просмотреть и редактировать его информацию.
     *
     * @param productId Идентификатор продукта.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы деталей продукта.
     */
    @GetMapping("/{productId}")
    public String detailProduct(@PathVariable Long productId,
                                @PathVariable Long organizationId,
                                @PathVariable Long employeeId,Model model) {
        model.addAttribute("product", productService.getProductById(productId, organizationId, employeeId));
        return "detailProduct";
    }

    /**
     * Отображает страницу для создания нового продукта или редактирования существующего.
     *
     * @param productId Идентификатор продукта для редактирования; null для создания нового.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы редактирования продукта.
     */
    @GetMapping("/edit/{productId}")
    public String editProduct(@PathVariable Long productId,
                              @PathVariable Long organizationId,
                              @PathVariable Long employeeId, Model model) {
        Product product = productId != null ? productService.getProductById(productId, organizationId, employeeId) : new Product();
        model.addAttribute("product", product);
        return "detailProduct";
    }

    /**
     * Сохраняет новый или обновлённый продукт.
     *
     * @param product Объект продукта, полученный из формы.
     * @return Перенаправление на список продуктов.
     */
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam Long organizationId,
                              @RequestParam Long employeeId) {
        logger.info("Saving product: {}", product);
        productService.saveOrUpdateProduct(product, organizationId, employeeId);
        return "redirect:/organization/" + organizationId + "/employee/" + employeeId + "/product/byOrganization/";
    }

    /**
     * Создаёт копию существующего продукта по его идентификатору.
     *
     * @param productId Идентификатор продукта для копирования.
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы деталей продукта для новой копии.
     */
    @GetMapping("/details/{productId}")
    public String viewProductDetails(@PathVariable Long productId,
                                     @PathVariable Long organizationId,
                                     @PathVariable Long employeeId,
                                     Model model) {
        Product product = productService.getProductById(productId, organizationId, employeeId);

        model.addAttribute("product", product);
        model.addAttribute("organizationId", organizationId);
        model.addAttribute("employeeId", employeeId);
        logger.info("Инфо product: {}", product);
        return "detailProduct";  // Assuming you have a Thymeleaf template named detailProduct.html
    }

    @PostMapping("/clone/{productId}")
    public String cloneProduct(@PathVariable Long productId,
                               @PathVariable Long organizationId,
                               @PathVariable Long employeeId,
                               Model model) {
        Product clonedProduct = productService.getProductById(productId, organizationId, employeeId);
        clonedProduct.setId(null);  // Ensure the cloned product has a null ID to be saved as a new product
        model.addAttribute("product", clonedProduct);
        return "detailProduct";
    }

    /**
     * Удаляет продукт по его идентификатору и перенаправляет на список продуктов.
     *
     * @param organizationId Идентификатор организации.
     * @param employeeId Идентификатор сотрудника.
     * @param productId Идентификатор продукта для удаления.
     * @return Перенаправление на список продуктов.
     */
    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId,
                                @PathVariable Long organizationId,
                                @PathVariable Long employeeId) {
        productService.deleteProduct(productId, organizationId, employeeId);
        return "redirect:/organization/" + organizationId + "/employee/" + employeeId + "/product/byOrganization/";
    }
}