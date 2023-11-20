//package com.example.ssm.models;
//
//import com.example.ssm.utils.CustomConstants;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "GATEWAY_ORDER_INVOICE")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class OrderInvoice {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID")
//    private Long id;
//
//    @Column(name = "DATE")
//    private String localDate;
//
////    @Column(name = "STATE")
////    private String state;
//
//    @Column(name = "STATE")
//    @Enumerated(EnumType.STRING)
//    private CustomConstants.OrderStates state;
//
//    @Column(name = "PAYMENT_TYPE")
//    String paymentType;
//
//    @Transient
//    String event;
//
//
//    public CustomConstants.OrderStates getState() {
//        return state;
//    }
//
//    public void setState(CustomConstants.OrderStates state) {
//        this.state = CustomConstants.OrderStates.valueOf(state.name());
//    }
//
//    @SneakyThrows
//    @Override
//    public String toString() {
//        return new ObjectMapper().writeValueAsString(this);
//    }
//}