package com.bank.BankSystem.controllers;

import com.bank.BankSystem.entity.Account;
import com.bank.BankSystem.entity.Transaction;
import com.bank.BankSystem.repositories.AccountRepository;
import com.bank.BankSystem.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @GetMapping
    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Transaction findById(@PathVariable Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        transaction.getReceiverAccount().getReceivedTransfers().remove(transaction);
        transaction.getSenderAccount().getSentTransfers().remove(transaction);

        accountRepository.save(transaction.getReceiverAccount());
        accountRepository.save(transaction.getSenderAccount());

        transactionRepository.delete(transaction);
        return ResponseEntity.ok("Transaction deleted!");
    }


    @PostMapping
    @Transactional
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {

        Account sender = accountRepository.findById(transaction.getSenderAccount().getId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account receiver = accountRepository.findById(transaction.getReceiverAccount().getId())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getBalance().compareTo(transaction.getAmount()) >= 0) {
            sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
            receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));

            transaction.setSenderAccount(sender);
            transaction.setReceiverAccount(receiver);

            sender.getSentTransfers().add(transaction);
            receiver.getReceivedTransfers().add(transaction);

            //accountRepository.save(sender);
            //accountRepository.save(receiver);

            transactionRepository.save(transaction);

            return ResponseEntity.ok("Transfer successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }
    }

    @PostMapping("/deposit")
    @Transactional
    public ResponseEntity<String> deposit(@RequestBody Transaction transaction){
        Account receiver = accountRepository.findById(transaction.getReceiverAccount().getId())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        transaction.setSenderAccount(null);
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));
        receiver.getReceivedTransfers().add(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(receiver);

        return ResponseEntity.ok("Money successfully deposited!");
    }

    @PostMapping("/withdraw")
    @Transactional
    public ResponseEntity<String> withdraw(@RequestBody Transaction transaction){
        Account sender = accountRepository.findById(transaction.getSenderAccount().getId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        transaction.setReceiverAccount(null);
        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        sender.getSentTransfers().add(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(sender);

        return ResponseEntity.ok("Money successfully withdrawn!");
    }
}
