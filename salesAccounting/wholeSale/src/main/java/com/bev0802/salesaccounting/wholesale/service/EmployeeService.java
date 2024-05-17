package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;

    @Value("${productDB.service.url}")
    private String productDBServiceUrl;

    @Autowired
    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

//    public Employee findById(Long employeeId, Long organizationId) {
//        String url = productDBServiceUrl + "/api/organization/"+ organizationId + "/employees/" + employeeId;
//        try {
//            Employee employee = restTemplate.getForObject(url, Employee.class);
//            return employee;
//        } catch (HttpClientErrorException e) {
//            // Здесь вы можете логировать ошибку или обрабатывать исключение в зависимости от вашей логики приложения
//            throw new RuntimeException("Не удалось найти сотрудника с ID: " + employeeId, e);
//        }
//    }

    public Employee getEmployeeById(Long employeeId, Long organizationId) {
        // Строим URL запроса, включая идентификатор сотрудника

        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/" + employeeId;

        // Отправляем GET запрос к productDB для получения сотрудника
        ResponseEntity<Employee> response = restTemplate.getForEntity(url, Employee.class);

        // Проверяем статус ответа
        if (response.getStatusCode() == HttpStatus.OK) {
            // Возвращаем объект сотрудника, если запрос выполнен успешно
            return response.getBody();
        } else {
            // Обработка случая, когда сотрудник не найден или произошла другая ошибка.
            // Можете выбросить исключение или обработать как требуется.
            return null;
        }
    }

    /**
     * Получает список сотрудников по идентификатору организации.
     *
     * @param organizationId Идентификатор организации.
     * @return Список сотрудников.
     * @throws ServiceException Если возникает ошибка при получении сотрудников.
     */
    public List<Employee> findEmployeesByOrganizationId(Long organizationId) {
        try {
            String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/getEmployeesByOrganizationId";
            Employee[] employees = restTemplate.getForObject(url, Employee[].class);
            assert employees != null;
            return Arrays.asList(employees);
        } catch (HttpClientErrorException e) {
            logger.error("Ошибка при получении сотрудников организации с ID {}.", organizationId, e);
            throw new ServiceException("Не удалось получить сотрудников организации: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Ошибка при получении сотрудников организации с ID {}.", organizationId, e);
            throw new ServiceException("Произошла ошибка при получении сотрудников организации.");
        }
    }

    // Метод для сохранения сотрудника
    public void saveEmployee(Long organizationId, Employee employee) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/register";
        restTemplate.postForEntity(url, employee, Employee.class);
    }

    // Метод для клонирования сотрудника
    public void cloneEmployee(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/clone/" + employeeId;
        restTemplate.postForEntity(url, null, Void.class);
    }

    // Метод для удаления сотрудника
    public void deleteEmployee(Long organizationId, Long employeeId) {
        String url = productDBServiceUrl + "/api/organization/" + organizationId + "/employee/delete/" + employeeId;
        restTemplate.postForEntity(url, null, Void.class);
    }

    // Дополнительные методы и классы
    public static class ServiceException extends RuntimeException {
        public ServiceException(String message) {
            super(message);
        }
    }
}


