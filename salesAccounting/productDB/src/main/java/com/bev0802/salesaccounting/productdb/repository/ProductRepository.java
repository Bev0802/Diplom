package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс репозитория для работы с объектами {@link Product} в базе данных.
 * Предоставляет базовые операции для работы с продуктами, такие как поиск, сохранение и удаление,
 * а также расширенные операции для поиска продуктов по определённым критериям.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Находит продукт по его точному имени.
     *
     * @param name Имя продукта для поиска.
     * @return Найденный продукт или {@code null}, если продукт не найден.
     */
    Product findByName(String name);

    /**
     * Находит список продуктов, игнорируя регистр букв в имени.
     *
     * @param name Имя продукта для поиска.
     * @return Список продуктов с совпадающим именем.
     */
    List<Product> findByNameIgnoreCase(String name);

    /**
     * Находит список продуктов, содержащих заданную подстроку в имени, игнорируя регистр букв.
     *
     * @param name Подстрока для поиска в имени продукта.
     * @return Список продуктов, содержащих указанную подстроку в имени.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Находит список продуктов, количество которых больше указанного значения.
     *
     * @param quantity Минимальное количество продукта для включения в результат.
     * @return Список продуктов с количеством больше указанного.
     */
    List<Product> findByQuantityGreaterThan(BigDecimal quantity);

    /**
     * Находит список продуктов с ценой в заданном диапазоне.
     *
     * @param priceStart Нижняя граница диапазона цен.
     * @param priceEnd Верхняя граница диапазона цен.
     * @return Список продуктов с ценой в указанном диапазоне.
     */
    List<Product> findByPriceBetween(BigDecimal priceStart, BigDecimal priceEnd);

    /**
     * Возвращает список товаров, принадлежащих определенной организации.
     * @param organizationId ID организации, которой принадлежат товары.
     * @return Список товаров, принадлежащих организации.
     */
    List<Product> findByOrganizationId(Long organizationId);

    void deleteAllByOrganization(Organization organization);


}

