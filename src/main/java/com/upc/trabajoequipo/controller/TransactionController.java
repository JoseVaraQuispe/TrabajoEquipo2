package com.upc.trabajoequipo.controller;

import com.upc.trabajoequipo.exception.ValidationException;
import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.model.Transaction;
import com.upc.trabajoequipo.repository.AccountRepository;
import com.upc.trabajoequipo.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class TransactionController {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    //Endpoint: http://localhost:8080/api/bank/v1/transactions/filterByNameCustomer
    //Method: Get
    @Transactional(readOnly = true)
    @RequestMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> getTransactionsByNameCustomer(@RequestParam(name="nameCustomer") String nameCustomer){
        return new ResponseEntity<List<Transaction>>(transactionRepository.findTransactionByNameCustomer(nameCustomer), HttpStatus.OK);
    }

    //Endpoint: http://localhost:8080/api/bank/v1/transactions/filterByCreateDateRange
    //Method: Get
    @Transactional(readOnly = true)
    @RequestMapping("/transactions/filterByCreateDateRange")
    public ResponseEntity<List<Transaction>> getTransactionsByCreateDate(@RequestParam(name="startDate") LocalDate startDate, @RequestParam(name="endDate") LocalDate endDate){
        if(startDate.isAfter(endDate))
            throw new ValidationException(("La fecha final debe ser despues a la fecha de inicio"));
        return new ResponseEntity<List<Transaction>>(transactionRepository.findTransactionByCreateDateRange(startDate, endDate), HttpStatus.OK);
    }


    //Endpoint: http://localhost:8080/api/bank/v1/accounts/{id}/transactions
    //Method: Post
    @Transactional
    @PostMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> createTransaction(@PathVariable(name="id") long account_id, @RequestBody Transaction transaction){
        Account account = accountRepository.findById(account_id).orElseThrow(()->new ResourceAccessException("No se encuentra la cuenta con el id: " + account_id));
        validateTransaction(transaction);
        if(transaction.getType().trim() == "Retiro")
            transaction.setBalance(transaction.getBalance() - transaction.getAmount());
        if(transaction.getType().trim() == "Deposito")
            transaction.setBalance(transaction.getBalance() + transaction.getAmount());
        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.CREATED);
    }


    private void validateTransaction(Transaction transaction){
        if(transaction.getType()==null || transaction.getType().trim().isEmpty())
            throw new ValidationException(("El tipo de transacción bancaria debe ser obligatorio"));
        if(transaction.getAmount() <= 0.0)
            throw new ValidationException(("El monto en una transacción bancaria debe ser mayor de S/.0.0"));
        if(transaction.getType().trim() == "Retiro" && transaction.getAmount() > transaction.getBalance())
            throw new ValidationException(("“En una transacción bancaria tipo retiro el monto no puede ser mayor al saldo"));
    }

}
