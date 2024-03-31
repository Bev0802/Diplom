package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Counterparty;
import com.bev0802.salesaccounting.productdb.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long>, JpaSpecificationExecutor<Counterparty> {

}
