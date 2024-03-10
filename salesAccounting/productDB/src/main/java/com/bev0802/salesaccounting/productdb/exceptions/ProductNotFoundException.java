package com.bev0802.salesaccounting.productdb.exceptions;
/**
 * Исключение, выбрасываемое когда запрашиваемый продукт не может быть найден в базе данных.
 * Это может произойти, например, при попытке доступа к продукту по идентификатору, который не существует.
 */
public class ProductNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания исключения с детальным сообщением о причине.
     * @param message Сообщение, описывающее причину исключения. Обычно содержит информацию
     *                о том, какой продукт не был найден и какие действия привели к этому.
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
