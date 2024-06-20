package com.bev0802.salesaccounting.wholesale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для обработки запросов на главную страницу и страницы логина.
 */
@Controller
public class HomeController {

    /**
     * Обрабатывает запросы на "/login" и возвращает страницу логина.
     *
     * @return имя представления index.html.
     */
    @RequestMapping("/login")
    public String index() {
        return "index.html";
    }
}
