package com.bev0802.salesaccounting.productdb.repository.specification;

import com.bev0802.salesaccounting.productdb.model.Organization;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecifications {

    public static Specification<Organization> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Organization> innContains(String inn) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("inn"), "%" + inn + "%");
    }
}