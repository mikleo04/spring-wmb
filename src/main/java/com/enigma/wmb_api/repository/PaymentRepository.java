package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
