package com.bev0802.salesaccounting.productdb.exceptions;
/**
 * Исключение, выбрасываемое при не найденном заказе.
 * Наследует RuntimeException.
 */
public class OrderNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
