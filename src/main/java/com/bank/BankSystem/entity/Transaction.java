package com.bank.BankSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="transactions_date")
    private LocalDateTime transactionDate;

    @Column(name="amount")
    private BigDecimal amount;

    @JsonBackReference("receiver-ref")
    @ManyToOne
    @JoinColumn(name = "receiving_id")
    private Account receiverAccount;

    @JsonBackReference("sender-ref")
    @ManyToOne
    @JoinColumn(name="sender_id")
    private Account senderAccount;

    @PrePersist
    public void prePersist() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}
