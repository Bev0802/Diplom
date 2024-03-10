package com.bev0802.salesaccounting.productdb.exceptions;
/**
 * Исключение, выбрасываемое в случае попытки удаления продукта,
 * который находится на складе (то есть его количество больше нуля).
 * Это предотвращает удаление продуктов, которые могут быть еще проданы.
 */
public class ProductInStockException extends RuntimeException {
    /**
     * Конструктор для создания исключения с детальным сообщением.
     * @param message Сообщение, описывающее причину исключения.
     */
    public ProductInStockException(String message) {
        super(message);
    }
}


