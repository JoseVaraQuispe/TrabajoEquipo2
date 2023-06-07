package com.upc.trabajoequipo.controller;

import com.upc.trabajoequipo.exception.ValidationException;
import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.upc.trabajoequipo.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    private final AccountRepository accountRepository;
    public AccountController(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    //EndPoint: http://localhost:5432/api/bank/v1/accounts
    //Method: GET
    @Transactional
    @RequestMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts(){
        return new ResponseEntity<List<Account>>(accountRepository.findAll(), HttpStatus.OK);
    }


    //EndPoint: http://localhost:5432/api/bank/v1/accounts
    //Method: POST
    @Transactional
    @PostMapping("/accounts")
    public ResponseEntity<Account> createBook(@RequestBody Account account){
        existsByNameCustomerAndNumberAccount(account);
        validateAccount(account);
        return new ResponseEntity<Account>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    public void validateAccount(Account account){
        if(account.getNameCustomer() == null || account.getNameCustomer().trim().isEmpty())
            throw new ValidationException("El nombre del cliente debe ser obligatorio");
        if(account.getNameCustomer().length()>30)
            throw new ValidationException("El nombre del cliente no debe exceder los 30 caracteres");

        if(account.getNumberAccount() == null || account.getNumberAccount().trim().isEmpty())
            throw new ValidationException("El número de cuenta debe ser obligatorio");
        if(account.getNumberAccount().length()!=13)
            throw new ValidationException("El número de cuenta debe tener una longitud de 13 caracteres");
    }

    private void existsByNameCustomerAndNumberAccount(Account account){
        if(accountRepository.existsByNameCustomerAndNumberAccount(account.getNameCustomer(), account.getNumberAccount())){
            throw new ValidationException("“No se puede registrar la cuenta porque ya existe uno con estos datos");
        }
    }
}
