package com.bev0802.salesaccounting.wholesale.controller;

import com.bev0802.salesaccounting.wholesale.model.Employee;
import com.bev0802.salesaccounting.wholesale.model.Organization;
import com.bev0802.salesaccounting.wholesale.service.EmployeeService;
import com.bev0802.salesaccounting.wholesale.service.OrganizationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/organization/{organizationId}/employee")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrganizationService organizationService;
    private final RestTemplate restTemplate;

    public EmployeeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate; }

    @GetMapping("/employeeLoginForm")
    public String showEmployeeLoginForm(@PathVariable("organizationId") Long organizationId, Model model) {
        // Подготавливаем данные для отображения страницы входа для сотрудника
        Organization organization = organizationService.findById(organizationId);

        model.addAttribute("organizationId", organizationId);
        model.addAttribute("organizationName", organization.getName());
        return "employeeLogin"; // Возвращаем имя файла шаблона страницы входа для сотрудника
    }

    @PostMapping("/authenticate/")
    public String authenticateEmployee(@RequestParam String email, @RequestParam String password, @PathVariable("organizationId") Long organizationId, Model model, HttpSession session) {
        // Строим URL для запроса к REST API
        String url = "http://productDB/api/organization/" + organizationId + "/employee/authenticate";

        // Подготавливаем данные для запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        try {
            // Отправляем запрос
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);
            // Логирование ответа
            System.out.println("Response Status: " + responseEntity.getStatusCode());
            System.out.println("Response Body: " + responseEntity.getBody());

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                Map<String, Object> responseBody = responseEntity.getBody();

                // Извлекаем employeeId из ответа
                Long employeeId = Long.valueOf(responseBody.get("employeeId").toString());
                session.setAttribute("employeeId", employeeId);

                // Дальнейшая логика...
                return "redirect:/organization/" + organizationId + "/employee/" + employeeId + "/startWork";
            } else {
                model.addAttribute("loginError", "Ошибка аутентификации.");
                return "redirect:/organization/" + organizationId + "/employee/employeeLoginForm";
            }
        } catch (RestClientException e) {
            model.addAttribute("loginError", "Техническая ошибка.");
            return "redirect:/organization/" + organizationId + "/employee/employeeLoginForm";
        }
    }

    @GetMapping("{employeeId}/startWork")
    public String startWork(@PathVariable("organizationId") Long organizationId, @PathVariable("employeeId") Long employeeId, HttpSession session, Model model) {
        // Подготавливаем данные для отображения страницы приветствия сотрудника
        //Employee employee = (Employee) session.getAttribute("employee"); // Получаем сотрудника из сессии
        //Long employeeId = (Long) session.getAttribute("employeeId"); // Получаем ID сотрудника из сессии
        Employee employee = employeeService.getEmployeeById(employeeId, organizationId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("EmployeeName", employee.getName());
        model.addAttribute("organizationName", employee.getOrganization().getName());
        logger.info("Employee ID from session: {}", employeeId);
        if (employeeId == null) {
            return "redirect:/organization/" + organizationId + "/employeeLogin"; // Если ID сотрудника не найден, возвращаем на страницу входа
        }
        return "startWork"; // Имя шаблона страницы приветствия сотрудника
    }

    @GetMapping("/listEmployees")
    public String listEmployees(@PathVariable("organizationId") Long organizationId, Model model) {
        List<Employee> employees = employeeService.findEmployeesByOrganizationId(organizationId);
        model.addAttribute("employees", employees);
        return "listEmployees";
    }

    // Метод для отображения формы создания нового сотрудника
    @GetMapping("/new")
    public String showCreateEmployeeForm(@PathVariable("organizationId") Long organizationId, Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("organizationId", organizationId);
        return "detailEmployees"; // Имя шаблона для формы создания сотрудника
    }

    // Метод для сохранения нового сотрудника
    @PostMapping("/save")
    public String saveEmployee(@PathVariable("organizationId") Long organizationId, @ModelAttribute Employee employee) {
        employeeService.saveEmployee(organizationId, employee);
        return "redirect:/organization/" + organizationId + "/employee/listEmployees";
    }

    // Метод для клонирования сотрудника
    @GetMapping("/clone/{employeeId}")
    public String cloneEmployee(@PathVariable("organizationId") Long organizationId, @PathVariable("employeeId") Long employeeId) {
        employeeService.cloneEmployee(organizationId, employeeId);
        return "redirect:/organization/" + organizationId + "/employee/listEmployees";
    }

    // Метод для удаления сотрудника
    @PostMapping("/delete/{employeeId}")
    public String deleteEmployee(@PathVariable("organizationId") Long organizationId, @PathVariable("employeeId") Long employeeId) {
        employeeService.deleteEmployee(organizationId, employeeId);
        return "redirect:/organization/" + organizationId + "/employee/listEmployees";
    }
}
