package com.upc.trabajoequipo.repository;
import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>    {
    boolean existsByNumberAccountAndAccountAndAccountTransaction(String numberAccount, Account account, boolean accountTransaction);
    List<Transaction> findByNameCustomer(String nameCustomer);
}
