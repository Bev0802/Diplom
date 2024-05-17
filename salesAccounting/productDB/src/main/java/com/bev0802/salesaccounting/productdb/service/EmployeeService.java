package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления сотрудниками в базе данных.
 */
@Service

public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Получение списка всех сотрудников
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    // Получение списка сотрудников по ID организации
    public List<Employee> findEmployeesByOrganizationId(Long organizationId) {

        return employeeRepository.findByOrganizationId(organizationId);
    }

    public Optional<Employee> authenticateEmployee(String email, String password, Long organizationId) {

        return employeeRepository.findByEmailAndPasswordAndOrganizationId(email, password, organizationId);
    }
    @Transactional
    public Employee registerEmployee(Employee employee, Long organizationId) {
        // Проверка на уникальность имени и электронной почты
        employeeRepository.findByNameOrEmail(employee.getName(), employee.getEmail())
                .ifPresent(e -> {
                    throw new DataIntegrityViolationException("Сотрудник с таким именем или электронной почтой уже существует.");
                });

        // Установка organizationId для сотрудника без извлечения организации из базы данных
        Organization organization = new Organization();
        organization.setId(organizationId);
        employee.setOrganization(organization);
        return employeeRepository.save(employee);
    }
    /*
    * Получение сотрудника по ID
    */
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    /*
    * Клонирование сотрудника
     */
    @Transactional
    public Employee cloneEmployee(Long employeeId, Long organizationId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isPresent()) {
            Employee originalEmployee = employeeOpt.get();
            Employee clonedEmployee = new Employee();
            clonedEmployee.setName(originalEmployee.getName());
            clonedEmployee.setPosition(originalEmployee.getPosition());
            clonedEmployee.setEmail(originalEmployee.getEmail());
            clonedEmployee.setPassword(originalEmployee.getPassword());
            Organization organization = new Organization();
            organization.setId(organizationId);
            clonedEmployee.setOrganization(organization);
            return employeeRepository.save(clonedEmployee);
        } else {
            throw new IllegalArgumentException("Сотрудник с ID " + employeeId + " не найден.");
        }
    }

    // Удаление сотрудника
    @Transactional
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

}

