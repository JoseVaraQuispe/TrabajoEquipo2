package com.upc.trabajoequipo.repository;

import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNameCustomerAndNumberAccount(String nameCustomer, String numberAccount);
}
