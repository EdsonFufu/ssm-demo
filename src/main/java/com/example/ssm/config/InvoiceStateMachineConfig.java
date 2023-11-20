package com.example.ssm.config;

import com.example.ssm.dto.InvoiceEvent;
import com.example.ssm.dto.InvoiceState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Optional;

import static com.example.ssm.dto.InvoiceEvent.*;
import static com.example.ssm.services.PaymentInvoiceService.INVOICE_ID;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableStateMachineFactory
public class InvoiceStateMachineConfig extends StateMachineConfigurerAdapter<InvoiceState, InvoiceEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<InvoiceState, InvoiceEvent> states) throws Exception {
        states.withStates()
                .initial(InvoiceState.INVOICE_RECEIVED)
                .states(EnumSet.allOf(InvoiceState.class))
               // .end(InvoiceState.INVOICE_PAID)
                .end(InvoiceState.SCREEN_REJECTED)
                .end(InvoiceState.SCREEN_HOLD)
                .end(InvoiceState.INVOICE_CANCELLED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<InvoiceState, InvoiceEvent> config) throws Exception {

        StateMachineListenerAdapter<InvoiceState, InvoiceEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<InvoiceState, InvoiceEvent> from, State<InvoiceState, InvoiceEvent> to) {
                super.stateChanged(from, to);
                Optional.ofNullable(from).ifPresent(fromState -> {
                    log.info("Change state from: %s to %s".formatted(fromState.getStates(),to.getStates()));
                });
            }
        };

        config.withConfiguration()
                .autoStartup(true)
                .listener(adapter);
    }



    @Override
    public void configure(StateMachineTransitionConfigurer<InvoiceState, InvoiceEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(InvoiceState.INVOICE_RECEIVED)
                .target(InvoiceState.SCREEN_ACCEPTED)
                .event(InvoiceEvent.SCREEN_INVOICE)
                .and()
                .withExternal()
                .source(InvoiceState.SCREEN_ACCEPTED)
                .target(InvoiceState.INVOICE_PAID)
                .event(InvoiceEvent.CONFIRM_INVOICE)
                .and()
                .withExternal()
                .source(InvoiceState.INVOICE_RECEIVED)
                .target(InvoiceState.SCREEN_REJECTED)
                .event(InvoiceEvent.SCREEN_INVOICE)
                .and()
                .withExternal()
                .source(InvoiceState.INVOICE_RECEIVED)
                .target(InvoiceState.SCREEN_TIMED_OUT)
                .event(InvoiceEvent.SCREEN_INVOICE)
                .and()
                .withExternal()
                .source(InvoiceState.INVOICE_RECEIVED)
                .target(InvoiceState.SCREEN_HOLD)
                .event(InvoiceEvent.SCREEN_INVOICE)
                .and()
                .withExternal()
                .source(InvoiceState.INVOICE_PAID)
                .target(InvoiceState.INVOICE_CANCELLED)
                .event(InvoiceEvent.CANCEL_INVOICE);
    }

    @Bean
    public Guard<InvoiceState, InvoiceEvent> invoiceIdguard() {
        return ctx -> ctx.getMessageHeader(INVOICE_ID) != null;
    }

    @Bean
    public Action<InvoiceState, InvoiceEvent> requestInvoiceAction() {
        return context -> {
            log.info("logging-request-invoice-action:{}", context.getEvent());
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(REQUEST_INVOICE).setHeader(INVOICE_ID, context.getMessageHeader(INVOICE_ID)).build());
        };
    }

    @Bean
    public Action<InvoiceState, InvoiceEvent> screenInvoceAction() {
        return context -> {
            log.info("logging-screen-invoice-action:{}", context.getEvent());
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(SCREEN_INVOICE).setHeader(INVOICE_ID, context.getMessageHeader(INVOICE_ID)).build());
        };
    }

    @Bean
    public Action<InvoiceState, InvoiceEvent> confirmInvoiceAction() {
        return context -> {
            log.info("logging-confirm-invoice-action:{}", context.getEvent());
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(CONFIRM_INVOICE).setHeader(INVOICE_ID, context.getMessageHeader(INVOICE_ID)).build());
        };
    }

    @Bean
    public Action<InvoiceState, InvoiceEvent> cancelInvoiceAction() {
        return context -> {
            log.info("logging-cancel-invoice-action:{}", context.getEvent());
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(CANCEL_INVOICE).setHeader(INVOICE_ID, context.getMessageHeader(INVOICE_ID)).build());
        };
    }


}