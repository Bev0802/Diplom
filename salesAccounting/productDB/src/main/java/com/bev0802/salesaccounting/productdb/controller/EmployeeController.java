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

/**
 * Контроллер для управления сотрудниками.
 * Обеспечивает API для операций CRUD и аутентификации сотрудников.
 */

@RestController
@RequestMapping("api/organization/{organizationId}/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Получает список всех сотрудников.
     *
     * @return список всех сотрудников
     */
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Получает список сотрудников по идентификатору организации.
     *
     * @param organizationId идентификатор организации
     * @return список сотрудников
     */
    @GetMapping("/getEmployeesByOrganizationId")
    public ResponseEntity<List<Employee>> getEmployeesByOrganizationId(@PathVariable Long organizationId) {
        List<Employee> employees = employeeService.findEmployeesByOrganizationId(organizationId);
        return ResponseEntity.ok(employees);
    }

    /**
     * Получает сотрудника по идентификатору.
     *
     * @param employeeId идентификатор сотрудника
     * @return сотрудник, если найден, иначе статус 404
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeService.findById(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Регистрирует нового сотрудника в указанной организации.
     *
     * @param organizationId идентификатор организации
     * @param employee данные сотрудника
     * @return зарегистрированный сотрудник или сообщение об ошибке
     */
    @PostMapping("/register")
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

    /**
     * Создает нового сотрудника в указанной организации.
     *
     * @param organizationId идентификатор организации
     * @param employee данные сотрудника
     * @return созданный сотрудник или сообщение об ошибке
     */
    @PostMapping("/new")
    public ResponseEntity<?> createEmployee(@PathVariable Long organizationId, @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.registerEmployee(employee, organizationId);
            return ResponseEntity.ok(savedEmployee);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(e.getMessage());
        }
    }
    /**
     * Клонирует сотрудника.
     *
     * @param organizationId идентификатор организации
     * @param employeeId идентификатор сотрудника для клонирования
     * @return клонированный сотрудник или сообщение об ошибке
     */
    @PostMapping("/clone/{employeeId}")
    public ResponseEntity<?> cloneEmployee(@PathVariable Long organizationId, @PathVariable Long employeeId) {
        try {
            Employee clonedEmployee = employeeService.cloneEmployee(employeeId, organizationId);
            return ResponseEntity.ok(clonedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось клонировать сотрудника.");
        }
    }

    /**
     * Удаляет сотрудника.
     *
     * @param employeeId идентификатор сотрудника для удаления
     * @return сообщение об успешном удалении или сообщение об ошибке
     */
    @PostMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        try {
            employeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok("Сотрудник успешно удален.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось удалить сотрудника.");
        }
    }

    /**
     * Аутентифицирует сотрудника.
     *
     * @param email электронная почта сотрудника
     * @param password пароль сотрудника
     * @param organizationId идентификатор организации
     * @return сообщение об успешной аутентификации и данные сотрудника или сообщение об ошибке
     */
    @PostMapping("/authenticate")
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
