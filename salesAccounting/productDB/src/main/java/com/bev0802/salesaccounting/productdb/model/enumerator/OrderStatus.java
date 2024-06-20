package com.bev0802.salesaccounting.productdb.model.enumerator;

/**
 * Перечисление для статусов заказа.
 * Содержит следующие статусы:
 * - NEW: Новый заказ
 * - CONFIRMED: Подтвержденный
 * - PAID: Оплаченный
 * - CANCELLED: Отмененный
 * - SHIPPED: Отгруженный
 */
public enum OrderStatus {
    NEW,          // Новый заказ
    CONFIRMED,    // Подтвержденный
    PAID,         // Оплаченный
    CANCELLED,     // Отмененный
    SHIPPED       // Отгруженный
}
