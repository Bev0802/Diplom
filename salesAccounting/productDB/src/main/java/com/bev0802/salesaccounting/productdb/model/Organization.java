package com.bev0802.salesaccounting.productdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String inn;
    private String kpp;
    private String address;
    private String email;
    private String password;

    @OneToMany(mappedBy = "organization")
    private Set<Product> products;

    @OneToMany(mappedBy = "organization")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "organization")
    private Set<Counterparty> counterparties;
}
