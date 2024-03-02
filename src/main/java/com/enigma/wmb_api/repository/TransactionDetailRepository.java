package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String > {
}
