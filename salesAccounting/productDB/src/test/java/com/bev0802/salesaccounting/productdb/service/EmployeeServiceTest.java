package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для EmployeeService.
 */
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    /**
     * Инициализация моков перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Тест для метода findAllEmployees.
     */
    @Test
    void findAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeService.findAllEmployees();

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    /**
     * Тест для метода findEmployeesByOrganizationId.
     */
    @Test
    void findEmployeesByOrganizationId() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        when(employeeRepository.findByOrganizationId(1L)).thenReturn(Arrays.asList(employee1, employee2));

        List<Employee> employees = employeeService.findEmployeesByOrganizationId(1L);

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findByOrganizationId(1L);
    }

    /**
     * Тест для метода authenticateEmployee.
     */
    @Test
    void authenticateEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.findByEmailAndPasswordAndOrganizationId("test@test.com", "password", 1L)).thenReturn(Optional.of(employee));

        Optional<Employee> authenticatedEmployee = employeeService.authenticateEmployee("test@test.com", "password", 1L);

        assertTrue(authenticatedEmployee.isPresent());
        verify(employeeRepository, times(1)).findByEmailAndPasswordAndOrganizationId("test@test.com", "password", 1L);
    }

    /**
     * Тест для метода registerEmployee.
     */
    @Test
    void registerEmployee() {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@test.com");
        Organization organization = new Organization();
        organization.setId(1L);
        employee.setOrganization(organization);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee registeredEmployee = employeeService.registerEmployee(employee, 1L);

        assertEquals(employee.getName(), registeredEmployee.getName());
        assertEquals(employee.getEmail(), registeredEmployee.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    /**
     * Тест для метода registerEmployee на случай дублирования данных.
     */
    @Test
    void registerEmployee_Duplicate() {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@test.com");

        when(employeeRepository.findByNameOrEmail("John Doe", "john.doe@test.com")).thenReturn(Optional.of(employee));

        assertThrows(DataIntegrityViolationException.class, () -> {
            employeeService.registerEmployee(employee, 1L);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    /**
     * Тест для метода findById.
     */
    @Test
    void findById() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.findById(1L);

        assertTrue(foundEmployee.isPresent());
        verify(employeeRepository, times(1)).findById(1L);
    }

    /**
     * Тест для метода cloneEmployee.
     */
//    @Test
//    void cloneEmployee() {
//        // Создаем originalEmployee с известными значениями
//        Employee originalEmployee = new Employee();
//        originalEmployee.setId(1L);
//        originalEmployee.setName("John Doe");
//        originalEmployee.setEmail("john.doe@test.com");
//        originalEmployee.setPassword("password");
//        originalEmployee.setPosition("Manager");
//
//        // Мокируем поведение репозитория
//        when(employeeRepository.findById(1L)).thenReturn(Optional.of(originalEmployee));
//        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());
//
//        // Вызываем метод сервиса для клонирования сотрудника
//        Employee clonedEmployee = employeeService.cloneEmployee(1L, 2L);
//
//        // Добавляем отладочный вывод
//        System.out.println("Имя оригинального сотрудника: " + originalEmployee.getName());
//        System.out.println("Имя клонированного сотрудника: " + (clonedEmployee != null ? clonedEmployee.getName() : "null"));
//
//        // Проверяем, что clonedEmployee не null
//        assertNotNull(clonedEmployee, "Клонированный сотрудник не должен быть null");
//
//        // Проверяем, что имена сотрудников совпадают
//        assertEquals(originalEmployee.getName(), clonedEmployee != null ? clonedEmployee.getName() : null,
//                "Имена сотрудников должны совпадать");
//
//        // Проверяем взаимодействия с репозиторием
//        verify(employeeRepository, times(1)).findById(1L);
//        verify(employeeRepository, times(1)).save(any(Employee.class));
//    }

    /**
     * Тест для метода deleteEmployee.
     */
    @Test
    void deleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
