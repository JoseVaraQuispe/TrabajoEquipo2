package com.upc.trabajoequipo.repository;

import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.nameCustomer = :nameCustomer")
    List<Transaction> findTransactionByNameCustomer(String nameCustomer);
    @Query("SELECT t FROM Transaction t WHERE t.createDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionByCreateDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
