package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Counterparty;
import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс репозитория для работы с объектами {@link Counterparty} в базе данных.
 * Предоставляет базовые операции для работы с контрагентами, такие как поиск, сохранение и удаление,
 * а также расширенные операции для поиска контрагентов по определённым критериям.
 */
@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long>, JpaSpecificationExecutor<Counterparty> {

    static void deleteByOrganization(Organization organization) {
    }

    void deleteAllByOrganization(Organization organization);
}
