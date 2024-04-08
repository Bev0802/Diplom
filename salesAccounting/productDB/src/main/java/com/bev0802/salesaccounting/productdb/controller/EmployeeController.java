package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Получение списка всех сотрудников
    @GetMapping("employees/")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Получение списка сотрудников по ID организации
    @GetMapping("/organizations/{organizationId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByOrganizationId(@PathVariable Long organizationId) {
        List<Employee> employees = employeeService.findEmployeesByOrganizationId(organizationId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeService.findById(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("organizations/{organizationId}/employees/register")
    public ResponseEntity<?> registerEmployee(@PathVariable Long organizationId, @RequestBody Employee employee) {
        try {
            // Передаем organizationId вместе с объектом employee в сервис для регистрации
            Employee savedEmployee = employeeService.registerEmployee(employee, organizationId);
            return ResponseEntity.ok(savedEmployee);
        } catch (DataIntegrityViolationException e) {
            // Обработка исключения и возврат клиенту сообщения об ошибке
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("organizations/{organizationId}/employees/authenticate")
    public ResponseEntity<?> authenticateEmployee(
            @RequestParam String email,
            @RequestParam String password,
            @PathVariable Long organizationId) { // Убедитесь, что используете @PathVariable для organizationId

        Optional<Employee> employee = employeeService.authenticateEmployee(email, password, organizationId);
        if (employee.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Аутентификация успешна");
            response.put("organizationId", employee.get().getOrganization().getId());
            response.put("employeeId", employee.get().getId());
            response.put("employeeName", employee.get().getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Сотрудник не найден или неверные учетные данные");
        }
    }
}
