package com.example.ssm.repositories;

import com.example.ssm.models.PaymentInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, Long> {
}