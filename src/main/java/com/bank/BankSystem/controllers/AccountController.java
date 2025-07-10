package com.bank.BankSystem.controllers;


import com.bank.BankSystem.entity.Account;
import com.bank.BankSystem.entity.User;
import com.bank.BankSystem.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping
    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        accountRepository.delete(account);
        return ResponseEntity.ok("Account successfully deleted!");
    }

    @PatchMapping("/{id}/setBalance")
    public Account updateBalance(@PathVariable Long id, @RequestBody Account accountAmount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        if(accountAmount.getBalance()!=null) account.setBalance(accountAmount.getBalance());

        return accountRepository.save(account);
    }



}
