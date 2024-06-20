package com.bev0802.salesaccounting.wholesale.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Base64;
/**
 * Класс BasicAuthInterceptor представляет собой интерцептор запросов для добавления HTTP Basic Authentication
 * заголовка к исходящему HTTP запросу.
 */
public class BasicAuthInterceptor implements ClientHttpRequestInterceptor {
    private final String username;
    private final String password;
    /**
     * Конструктор класса.
     *
     * @param username Имя пользователя для аутентификации.
     * @param password Пароль для аутентификации.
     */
    public BasicAuthInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * Метод перехвата HTTP запроса и добавления заголовка Authorization с использованием Basic Authentication.
     *
     * @param request   HTTP запрос.
     * @param body      Тело запроса.
     * @param execution Исполнение HTTP запроса.
     * @return HTTP ответ.
     * @throws IOException В случае ошибки ввода-вывода.
     */
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException, IOException {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        request.getHeaders().add("Authorization", authHeader);
        return execution.execute(request, body);
    }
}

