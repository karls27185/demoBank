package com.example.demo.repository;

import com.example.demo.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
    Optional<BankTransaction> findByTransactionReference(String transactionReference);
}
