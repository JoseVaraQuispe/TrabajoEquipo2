package com.upc.trabajoequipo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name_customer", nullable = false, length = 30)
    private String nameCustomer;
    @Column(name="number_account", nullable = false, length = 13)
    private String numberAccount;
}
