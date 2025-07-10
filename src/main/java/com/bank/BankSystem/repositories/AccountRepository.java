package com.bank.BankSystem.repositories;

import com.bank.BankSystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
