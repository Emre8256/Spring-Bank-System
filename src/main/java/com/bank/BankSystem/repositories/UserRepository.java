package com.bank.BankSystem.repositories;

import com.bank.BankSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
