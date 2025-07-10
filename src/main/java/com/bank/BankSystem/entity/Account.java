package com.bank.BankSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Data
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name="iban",nullable = false,unique = true)
    private String iban;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private AccountType type;
    //CURRENT,SAVINGS,CREDIT,INVESTMENT...

    @Column(name="currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;
    //TRY,USD,EUR...

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @Column(name="created_date")
    private LocalDate createdDate ;

    @JsonManagedReference("receiver-ref")
    @OneToMany(mappedBy = "receiverAccount", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private List<Transaction> receivedTransfers = new ArrayList<>();

    @JsonManagedReference("sender-ref")
    @OneToMany(mappedBy = "senderAccount", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private List<Transaction> sentTransfers = new ArrayList<>();




    @PrePersist
    public void prePersist() {
        if (this.iban == null) {
            this.iban = generateRandomIban();
        }
        if(this.createdDate==null){
            this.createdDate=LocalDate.now();
        }
    }

    private static final Random random = new Random();
    private String generateRandomIban() {
        String countryCode = "TR";
        String bankCode = "0006";
        String randomAccount = String.format("%016d", random.nextLong() & Long.MAX_VALUE);
        return countryCode + bankCode + randomAccount.substring(0, 16);
    }

    public enum AccountType {
        CURRENT, SAVINGS, CREDIT, INVESTMENT
        //more can be added
    }

    public enum Currency {
        TRY, USD, EUR
        //more can be added
    }
}
