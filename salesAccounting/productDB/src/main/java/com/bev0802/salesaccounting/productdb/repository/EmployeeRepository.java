package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для работы с объектами {@link Employee} в базе данных.
 * Предоставляет базовые операции для работы с сотрудниками, такие как поиск, сохранение и удаление,
 * а также расширенные операции для поиска сотрудников по определённым критериям.*
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

}
