package com.bev0802.salesaccounting.wholesale.controller;

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
@RequestMapping("product")
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
     * Метод для получения списка товаров, принадлежащих определенной организации для покупки
     * @param organizationId ID организации
     * @param model модель для передачи данных в представление
     * @return имя HTML шаблона для отображения списка товаров
     */
    @GetMapping("byOrganization/{organizationId}")
        public String getProductsByOrganization (@PathVariable("organizationId") Long organizationId, Model model) {
            List<Product> products = productService.findProductsByOrganization(organizationId);
            model.addAttribute("products", products);
            model.addAttribute("organizationId", organizationId);
            return "listProducts";
        }

    /**
     * Получает список товаров, доступных для покупки в определенной организации.
     * @param organizationId
     * @return возвращает список доступных для покупки
     */
    @GetMapping("availableForPurchase/{organizationId}")
    public String productsAvailableForPurchase(@PathVariable("organizationId") Long organizationId, HttpSession session, Model model) {
        List<Product> products = productService.findProductsNotBelongingToOrganization(organizationId);
        Organization organization = organizationService.findById(organizationId);

        logger.info("Products available for purchase: {}", products);
        logger.info("Products available for purchase size: {}", products.size());
        logger.info("Organization ID: {}", organizationId);

        model.addAttribute("products", products);
        model.addAttribute("organizationId", organizationId);

//        model.addAttribute("organizationName", products.getOrganization().getName());
    return "market"; // Имя шаблона для отображения списка товаров
    }

    /**
     * Переход на страницу создания нового продукта.
     *
     * @param model модель для передачи данных в представление.
     * @return имя HTML шаблона для создания нового продукта.
     */
    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "detailProduct"; // Переход на страницу создания нового продукта
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
     * @param id Идентификатор продукта.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы деталей продукта.
     */
    @GetMapping("/{id}")
    public String detailProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "detailProduct";
    }

    /**
     * Отображает страницу для создания нового продукта или редактирования существующего.
     *
     * @param id Идентификатор продукта для редактирования; null для создания нового.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы редактирования продукта.
     */
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = id != null ? productService.getProductById(id) : new Product();
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
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveOrUpdateProduct(product);
        return "redirect:/byOrganization/" + product.getOrganization().getId();
    }

    /**
     * Создаёт копию существующего продукта по его идентификатору.
     *
     * @param id Идентификатор продукта для копирования.
     * @param model Модель для передачи данных в представление.
     * @return Имя шаблона страницы деталей продукта для новой копии.
     */
    @GetMapping("/clone/{id}")
    public String cloneProduct(@PathVariable Long id, Model model) {
        Product clonedProduct = productService.cloneProduct(id);
        model.addAttribute("product", clonedProduct);
        return "detailProduct";
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта для удаления.
     * @return Перенаправление на список продуктов.
     */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product/";
    }



}
