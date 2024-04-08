package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс репозитория для работы с объектами {@link Organization} в базе данных.
 * Представляет базовые операции для работы с организациями, такие как поиск, сохранение и удаление,
 * а также расширенные операции для поиска организаций по определённым критериям.*
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    static void delete(Long id, Organization organization) {
    }

    Optional<Organization> findByInn(String inn);

    Optional<Object> findByEmail(String email);

    Optional<Organization> findByInnAndPassword(String inn, String password);

}
