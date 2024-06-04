package com.bev0802.salesaccounting.wholesale.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientErrorException(HttpClientErrorException ex, RedirectAttributes redirectAttributes) {
        String responseBody = ex.getResponseBodyAsString();

        if (responseBody.contains("is in stock and cannot be deleted")) {
            String userFriendlyMessage = "Вы не можете удалить эту позицию. Она еще есть на складе или участвует в документообороте.";
            redirectAttributes.addFlashAttribute("errorMessage", userFriendlyMessage);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", responseBody);
        }

        return "redirect:/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка: " + ex.getMessage());
        return "redirect:/error";
    }
}
