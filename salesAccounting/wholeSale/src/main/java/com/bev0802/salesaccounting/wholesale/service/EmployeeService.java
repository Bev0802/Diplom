package com.bev0802.salesaccounting.wholesale.service;

import com.bev0802.salesaccounting.wholesale.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    public Employee findById(Long employeeId) {
        String url = productDBServiceUrl + "/api/employees/" + employeeId;
        try {
            Employee employee = restTemplate.getForObject(url, Employee.class);
            return employee;
        } catch (HttpClientErrorException e) {
            // Здесь вы можете логировать ошибку или обрабатывать исключение в зависимости от вашей логики приложения
            throw new RuntimeException("Не удалось найти сотрудника с ID: " + employeeId, e);
        }
    }

//    public Employee authenticateEmployee(String email, String password, Long organizationId) {
//        // Строим URL запроса
//        String url = productDBServiceUrl + "/api/organizations/" + organizationId + "/employees/authenticate";
//
//        // Создаём объект запроса с данными для аутентификации
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("email", email);
//        map.add("password", password);
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
//
//        // Отправляем POST запрос на аутентификацию и ожидаем в ответе объект Employee
//        try {
//            ResponseEntity<Employee> response = restTemplate.postForEntity(url, requestEntity, Employee.class);
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                // Возвращаем объект сотрудника, если аутентификация прошла успешно
//                return response.getBody();
//            } else {
//                // Логирование ошибки или другая обработка
//                return null; // или выбрасываем исключение
//            }
//        } catch (HttpClientErrorException e) {
//            // Логирование ошибки или другая обработка
//            return null; // или выбрасываем исключение
//        }
//    }

    public Employee getEmployeeById(Long employeeId) {
        // Строим URL запроса, включая идентификатор сотрудника
        String url = productDBServiceUrl + "/employees/" + employeeId;

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

    public List<Employee> findEmployeesByOrganizationId(Long organizationId) {
        // Строим URL запроса, включая ID организации
        String url = productDBServiceUrl + "/api/organizations/" + organizationId + "/employees";

        // Создаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Отправляем GET запрос и получаем ответ
        ResponseEntity<Employee[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Employee[].class);

        // Проверяем статус ответа и возвращаем полученный список сотрудников
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            // В случае, если тело ответа null или статус ответа не OK, возвращаем пустой список
            // Можно также добавить обработку различных HTTP статусов, если это необходимо
            return List.of();
        }
    }
}