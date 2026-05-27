package com.example.payments_api.repository; // Ajusta o package se necessário

import com.example.payments_api.model.Payment;
import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    
    Optional<Payment> findByReference(String reference);
}