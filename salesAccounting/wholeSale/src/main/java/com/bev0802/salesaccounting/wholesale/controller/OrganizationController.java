package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Organization;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/organization/{organizationId}")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    /**
     * Метод выводит список контрагентов (организаций), кроме той, которая вошла в систему.
     *
     * @param organizationId Идентификатор организации.
     * @param model          Модель для передачи данных в представление.
     * @return Имя файла шаблона для отображения списка контрагентов.
     */
    @GetMapping("/listCounterparty")
    public String listCounterparty(@PathVariable("organizationId") Long organizationId, Model model) {
        List<Organization> organizations = organizationService.listCounterparty(organizationId);
        model.addAttribute("organizations", organizations);

        return "listCounterparty";
    }

    /**
     * Выводит детали контрагента (организации), кроме пароля. Данные нельзя редактировать.
     *
     * @param organizationId Идентификатор организации.
     * @param model          Модель для передачи данных в представление.
     * @return Имя файла шаблона для отображения деталей контрагента.
     */
    @GetMapping("/detailCounterparty/{counterpartId}")
    public String detailCounterparty(@PathVariable("counterpartId") Long organizationId, Model model) {
        // Получение данных контрагента (например, по ID) и добавление их в модель
        Organization counterparty = organizationService.detailCounterparty(organizationId);
        model.addAttribute("counterparty", counterparty);
        return "detailCounterparty";
    }

    /**
     * Метод выводит детали организации, которая вошла в систему, и данные можно редактировать.
     *
     * @param organizationId Идентификатор организации.
     * @param model Модель для передачи данных в представление.
     * @return Имя файла шаблона для отображения деталей организации.
     */
    @GetMapping("/detailOrganization")
    public String detailOrganization(@PathVariable("organizationId") Long organizationId, Model model) {
        Organization organization = organizationService.findById(organizationId);
        model.addAttribute("organization", organization);
        return "detailOrganization";
    }
}
