package com.upc.trabajoequipo.repository;

import com.upc.trabajoequipo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNameCustomerAndNumberAccount(String nameCustomer, String numberAccount);
}
