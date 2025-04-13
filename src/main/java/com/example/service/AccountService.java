package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Optional<Account> register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return Optional.empty(); 
        }

        if (accountRepo.findByUsername(account.getUsername()).isPresent()) {
            return Optional.empty(); 
        }

        return Optional.of(accountRepo.save(account));
    }

    public Optional<Account> login(Account loginAccount) {
        Optional<Account> found = accountRepo.findByUsername(loginAccount.getUsername());

        if (found.isPresent() && found.get().getPassword().equals(loginAccount.getPassword())) {
            return found;
        }

        return Optional.empty();
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepo.findByUsername(username);
    }
}
