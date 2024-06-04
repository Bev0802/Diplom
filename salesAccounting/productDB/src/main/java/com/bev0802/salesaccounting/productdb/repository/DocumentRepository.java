package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.Document;
import com.bev0802.salesaccounting.productdb.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    Document findTopBySellerOrganizationOrderByDocumentDateDesc(Organization sellerOrganization);

    List<Document> findBySellerOrganizationId(Long sellerOrganizationId);

    List<Document> findByBuyerOrganizationId(Long buyerOrganizationId);

}
