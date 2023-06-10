package com.upc.trabajoequipo.controller;

import com.upc.trabajoequipo.exception.ResourceNotFoundException;
import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.model.Transaction;
import com.upc.trabajoequipo.repository.AccountRepository;
import com.upc.trabajoequipo.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class TransactionController {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public TransactionController(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    //EndPoint hhtp://localhost:5432/api/bank/v1/transactions/filterByNameCustomer
    //Method: GET
    @Transactional
    @RequestMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> getTransactionsByNumberAccount(@RequestParam String numberAccount) {
        List<Transaction> transactions = TransactionRepository.findByNumberAccount(numberAccount);
        return ResponseEntity.ok(transactions);
    }

    //EndPoint hhtp://localhost:5432/api/bank/v1/accounts/{id}/transactions
    //Method: Post
    @Transactional
    @RequestMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> createTransaction(@PathVariable(value = "id") long accountId,
                                                         @RequestBody Transaction transaction) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la cuenta con el id: " + accountId));
        existsByNumberAccountAndAccountAndAccountTransaction(transaction, account);
        validateTransaction(transaction);

        return new ResponseEntity<Transaction>(TransactionRepository.save(transaction), HttpStatus.CREATED);
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getType() == null || transaction.getType().isEmpty()) {
            throw new RuntimeException("El tipo de transaccion es obligatorio");
        }
        double amount = transaction.getAmount();
        double balance = transaction.getBalance();
        if (amount <= 0.0) {
            throw new RuntimeException("El monto en una transacción bancaria debe ser mayor de S/.0.0");
        }
        if (transaction.getType().equalsIgnoreCase("Retiro") && amount > balance) {
            throw new RuntimeException("En una transacción bancaria tipo retiro el monto no puede ser mayor al saldo");
        }

        if (transaction.getType().equalsIgnoreCase("Deposito")) {
            double newBalance = balance + amount;
            transaction.setBalance(newBalance);
        }
        if (transaction.getType().equalsIgnoreCase("RETIRO")) {
            if (amount <= balance) {
                double newBalance = balance - amount;
                transaction.setBalance(newBalance);
            }

        }

    }
}