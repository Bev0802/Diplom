package com.bev0802.salesaccounting.wholesale.exception;

/**
 * Исключение, представляющее ошибки в бизнес-логике приложения.
 * Может использоваться для обозначения различных ошибок сервисного слоя.
 */
public class ServiceException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public ServiceException(String message) {
        super(message);
    }
    /**
     * Конструктор исключения с сообщением и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause Причина исключения.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}