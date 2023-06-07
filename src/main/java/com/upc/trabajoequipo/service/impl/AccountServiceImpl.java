package com.upc.trabajoequipo.service.impl;

import com.upc.trabajoequipo.model.Account;
import com.upc.trabajoequipo.repository.AccountRepository;
import com.upc.trabajoequipo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account){
        return accountRepository.save(account);
    }
}
