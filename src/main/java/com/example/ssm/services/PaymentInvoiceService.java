package com.example.ssm.services;

import com.example.ssm.components.PaymentInvoiceStateChangeInterceptor;
import com.example.ssm.dto.InvoiceEvent;
import com.example.ssm.dto.InvoiceState;
import com.example.ssm.models.PaymentInvoice;
import com.example.ssm.repositories.PaymentInvoiceRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.ssm.dto.InvoiceEvent.*;


@Slf4j
@Service
public class PaymentInvoiceService {
    public static final String INVOICE_ID = "invoiceId";


    @Builder
    private record PaymentResponse (long invoiceId, String state) {}


    @Autowired
    private StateMachineFactory<InvoiceState, InvoiceEvent> stateMachineFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private PaymentInvoiceRepository paymentInvoiceRepository;

    @Autowired
    private PaymentInvoiceStateChangeInterceptor paymentInvoiceStateChangeInterceptor;

    public PaymentResponse newPayment(PaymentInvoice paymentInvoice) {
        paymentInvoice.setTransactionDate(LocalDateTime.now().toString());
        paymentInvoice.setState(InvoiceState.INVOICE_RECEIVED);
        paymentInvoice.setEvent(REQUEST_INVOICE);
        PaymentInvoice newPayment = paymentInvoiceRepository.save(paymentInvoice);
        log.info("New Payment From DB:[{}]", newPayment);
        return PaymentResponse.builder().invoiceId(newPayment.getId()).state(newPayment.getState().name()).build();
    }

    @Transactional
    public PaymentResponse screen(Long invoiceId) {
       try {
           StateMachine<InvoiceState, InvoiceEvent> sm = build(invoiceId);

           sendEvent(invoiceId,sm,SCREEN_INVOICE);

           return PaymentResponse.builder().invoiceId(invoiceId).state(sm.getState().getId().name()).build();
       }catch (Exception e){
           log.info("Screen Failed:{}",e.getMessage());
           e.printStackTrace();
           throw e;
       }
    }



    @Transactional
    public  PaymentResponse confirm(Long invoiceId) {
        try {
            StateMachine<InvoiceState, InvoiceEvent> sm = build(invoiceId);

            sendEvent(invoiceId,sm,CONFIRM_INVOICE);

             return PaymentResponse.builder().invoiceId(invoiceId).state(sm.getState().getId().name()).build();
        }catch (Exception e){
            log.info("Confirm Invoice Failed:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public  PaymentResponse cancel(Long invoiceId) {
        try {
            StateMachine<InvoiceState, InvoiceEvent> sm = build(invoiceId);

            sendEvent(invoiceId,sm,CANCEL_INVOICE);

            return PaymentResponse.builder().invoiceId(invoiceId).state(sm.getState().getId().name()).build();
        }catch (Exception e){
            log.info("Cancelled Failed:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void sendEvent(Long paymentId,StateMachine<InvoiceState,InvoiceEvent> sm,InvoiceEvent event){
        Message message = MessageBuilder.withPayload(event)
                .setHeader(INVOICE_ID,paymentId)
                .build();
        sm.sendEvent(message);
    }

    public  StateMachine<InvoiceState, InvoiceEvent> build(Long orderId){
        PaymentInvoice paymentInvoice = paymentInvoiceRepository.getReferenceById(orderId);
        String orderIdKey = Long.toString(paymentInvoice.getId());
        var stateMachine =  stateMachineFactory.getStateMachine(orderIdKey);

        stateMachine.stop();

        stateMachine.getStateMachineAccessor()

                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(paymentInvoiceStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(paymentInvoice.getState(), null, null, null));});
        stateMachine.start();
        return stateMachine;
    }

}




