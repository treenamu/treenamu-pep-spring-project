package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidLoginException;
import com.example.exception.UsernameOrPasswordInvalidException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    // Inject account repository
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account newAccount(Account newAccount) throws UsernameOrPasswordInvalidException, DuplicateUsernameException {
        // Username or Password Invalid Exception
        // - Username must not be blank
        // - Password must be at least 4 characters long
        if (newAccount.getUsername() == "" || 
        newAccount.getUsername() == null || 
        newAccount.getPassword().length() < 4) {
            throw new UsernameOrPasswordInvalidException();
        }

        // Duplicate Username Exception
        // - Username must not already exist in database
        if (accountRepository.findByUsername(newAccount.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }

        return accountRepository.save(newAccount);
    }

    public Account login(Account loginInfo) throws InvalidLoginException {
        Account loggedInAccount = accountRepository.findByUsernameAndPassword(loginInfo.getUsername(), loginInfo.getPassword()).orElseThrow(() -> new InvalidLoginException());

        return loggedInAccount;
    }
}
