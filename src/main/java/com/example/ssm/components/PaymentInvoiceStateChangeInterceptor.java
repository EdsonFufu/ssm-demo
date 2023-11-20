package com.example.ssm.components;

import com.example.ssm.dto.InvoiceEvent;
import com.example.ssm.dto.InvoiceState;
import com.example.ssm.models.PaymentInvoice;
import com.example.ssm.repositories.PaymentInvoiceRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.example.ssm.services.PaymentInvoiceService.INVOICE_ID;


@Component
public class PaymentInvoiceStateChangeInterceptor extends StateMachineInterceptorAdapter<InvoiceState, InvoiceEvent> {
    private final PaymentInvoiceRepository paymentInvoiceRepository;

    public PaymentInvoiceStateChangeInterceptor(PaymentInvoiceRepository paymentInvoiceRepository) {
        this.paymentInvoiceRepository = paymentInvoiceRepository;
    }


    @Override
    public void preStateChange(State<InvoiceState, InvoiceEvent> state, Message<InvoiceEvent> message, Transition<InvoiceState, InvoiceEvent> transition, StateMachine<InvoiceState, InvoiceEvent> stateMachine, StateMachine<InvoiceState, InvoiceEvent> rootStateMachine) {
//        super.preStateChange(state, message, transition, stateMachine, rootStateMachine);
        Optional.ofNullable(message).flatMap(msg -> Optional.ofNullable(Objects.toString(msg.getHeaders().get(INVOICE_ID),"-1L"))).ifPresent(paymentId -> {
            PaymentInvoice payment = paymentInvoiceRepository.getReferenceById(Long.valueOf(paymentId));
            System.out.println("PreStateChange:[" +payment+"] => new State[" + state.getId() +"]");
            payment.setState(state.getId());
            System.out.println("Payment Invoice After Change State:["+payment+"] => new State[" + state.getId() +"]");
            PaymentInvoice paymentSaved = paymentInvoiceRepository.save(payment);
            System.out.println("Payment Invoice After Save:["+paymentSaved+"]");
        });
    }
}
