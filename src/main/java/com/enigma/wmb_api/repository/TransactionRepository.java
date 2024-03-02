package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String > {
}
