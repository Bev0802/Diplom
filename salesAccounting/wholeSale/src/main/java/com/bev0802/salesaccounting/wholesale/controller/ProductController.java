package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Product;
import com.bev0802.salesaccounting.wholesale.service.ProductService;
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

    @Autowired
    private ProductService productService;

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
            Model model) {
        model.addAttribute("products", productService.findProductsFiltered(name, available, startPrice, endPrice));
        return "listProducts";
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
        return "redirect:/product/";
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
