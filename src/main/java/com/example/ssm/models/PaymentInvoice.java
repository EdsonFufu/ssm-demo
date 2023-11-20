package com.example.ssm.models;

import com.example.ssm.dto.InvoiceEvent;
import com.example.ssm.dto.InvoiceState;
import com.example.ssm.utils.CustomConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "GATEWAY_MASTERCARD_CASH_PICKUP_PAYMENT_INVOICE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_NO")
    private String orderNumber;

    @Column(name = "TRANSACTION_DATE")
    private String transactionDate;


    @Column(name = "SENDER")
    String sender;

    @Column(name = "RECEIVER")
    String receiver;

    @Column(name = "AMOUNT")
    String amount;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private InvoiceState state;


    @Enumerated(EnumType.STRING)
    @Transient
    InvoiceEvent event;


    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}