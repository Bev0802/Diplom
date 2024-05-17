package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с объектами {@link Employee} в базе данных.
 * Предоставляет базовые операции для работы с сотрудниками, такие как поиск, сохранение и удаление,
 * а также расширенные операции для поиска сотрудников по определённым критериям.*
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    List<Employee> findByOrganizationId(Long organizationId);
    Optional<Employee> findByEmailAndPasswordAndOrganizationId
                        (String email, String password, Long organizationId);

    List<Employee> findAllByOrganization(Organization organization);
    void deleteAllByOrganization(Organization organization);

    Optional<Object> findByNameOrEmail(String name, String email);
}
