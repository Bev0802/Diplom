package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;


    @GetMapping("/register")
    public String registerForm(Model model) {
        // Добавьте любые атрибуты модели, если это необходимо
        return "detailOrganization"; // Имя файла вашего шаблона для регистрации организации
    }

    @GetMapping("/")
    public String listOrganizations(Model model) {
        model.addAttribute("organizations", organizationService.findAll());
        // Возвращаем имя файла шаблона для отображения списка организаций
        return "listOrganizations";
    }
}
