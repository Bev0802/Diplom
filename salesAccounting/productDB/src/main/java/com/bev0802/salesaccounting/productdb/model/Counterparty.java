package com.bev0802.salesaccounting.productdb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String inn;
    private String kpp;
    private String address;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
