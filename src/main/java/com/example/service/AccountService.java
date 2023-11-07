package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> addAccount(Account account) {

        // check if account already exists
        Optional<Account> foundAccount = accountRepository.findAccountByUsername(account.getUsername());
        boolean found = false;
        if (foundAccount.isPresent()) {
            found = true;
        }

        // if account doesn't exist and meets other criteria, add to db
        if (!account.getUsername().isBlank() &&
            account.getPassword().length() >= 4 && !found) {
                return Optional.of(accountRepository.save(account));
            }

        // if account is already found, return an account object that indicates so
        if (found) {
            return Optional.of(new Account(null, null));
        }
        // else return null if something else went wrong
        return Optional.empty();
    }

    public Optional<Account> login(Account account) {

        Optional<Account> possibleAccount = accountRepository.findAccountByUsername(account.getUsername());

        // if record from db has same username and password
        if (possibleAccount.isPresent() && possibleAccount.get().getPassword().equals(account.getPassword())) {
            return possibleAccount;
        }

        return Optional.empty();
    }
}
