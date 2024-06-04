package com.bev0802.salesaccounting.wholesale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для завершения работы приложения
 */
@Controller
    @RequestMapping("/logout")
    public class LogoutController {
    /**
     * Метод завершает работу текущего пользователя и перенаправляет на главную страницу ввода логина
     * @return /login
     */

    @GetMapping("/exit")
    public String exit() {
        // Завершение работы текущего пользователя
        return "redirect:/login"; // Перенаправление на главную страницу
    }

    /**
     * Метод полнотю завершает работу приложения
     * @return
     */
    @GetMapping("/close")
    public String close() {
        // Запуск нового потока для задержки перед завершением работы приложения
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Задержка для выполнения JavaScript
                shutdownApplication(); // Завершение работы приложения после задержки
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Возвращение страницы с JavaScript для перенаправления
        return "redirect:https://www.google.com"; // Перенаправление на главную страницу
    }

    private void shutdownApplication() {
            // Логика для завершения работы приложения
            System.exit(0);
    }
}

