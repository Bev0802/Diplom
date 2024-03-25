package com.bev0802.salesaccounting.wholesale.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

/**
 * Глобальный обработчик исключений для приложения.
 * Отлавливает исключения, возникающие во время HTTP запросов, и возвращает пользователю понятные сообщения об ошибке.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Обрабатывает исключения, возникающие при ошибке клиента HTTP.
     *
     * @param ex Исключение, возникшее в результате HTTP запроса.
     * @return ResponseEntity с сообщением об ошибке для пользователя.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        String responseBody = ex.getResponseBodyAsString();

        // Проверяем содержит ли ответ API указание на то, что товар не может быть удален
        // Изменено условие проверки, чтобы оно не зависело от переменной `id`
        if (responseBody.contains("is in stock and cannot be deleted")) {
            String userFriendlyMessage = "Вы не можете удалить эту позицию. Она еще есть на складе или участвует в документообороте.";
            return new ResponseEntity<>(Collections.singletonMap("errorMessage", userFriendlyMessage), ex.getStatusCode());
        }

        // Возвращаем оригинальное тело ошибки для всех остальных случаев
        return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
    }
}
