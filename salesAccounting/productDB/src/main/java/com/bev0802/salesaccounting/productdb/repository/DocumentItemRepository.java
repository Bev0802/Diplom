package com.bev0802.salesaccounting.productdb.repository;

import com.bev0802.salesaccounting.productdb.model.DocumentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentItemRepository extends JpaRepository<DocumentItem, Long>,
                                                JpaSpecificationExecutor<DocumentItem>{
    List<DocumentItem> findItemsByDocumentId(Long documentId);
}
