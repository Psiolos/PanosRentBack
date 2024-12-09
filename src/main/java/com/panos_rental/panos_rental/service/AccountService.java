package com.panos_rental.panos_rental.service;

import com.panos_rental.panos_rental.entity.Account;
import com.panos_rental.panos_rental.entity.Role;
import com.panos_rental.panos_rental.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }


    public Account createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setRole(Role.CLIENT);  //mono client mporoyn na dimiourgh8oun apo to register
        account.setEnabled(true);
        return accountRepository.save(account);
    }

    public void updatePoints(String username, int points) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (points < 0) {
                throw new IllegalArgumentException("Points cannot be negative");
            }
            account.setPoints(points);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
    }



}
