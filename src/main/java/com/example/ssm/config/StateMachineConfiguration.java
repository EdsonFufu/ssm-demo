//package com.example.ssm.config;
//
//import com.example.ssm.utils.CustomConstants;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.statemachine.action.Action;
//import org.springframework.statemachine.config.EnableStateMachineFactory;
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//import org.springframework.statemachine.guard.Guard;
//import org.springframework.statemachine.listener.StateMachineListenerAdapter;
//import org.springframework.statemachine.state.State;
//
//import java.util.EnumSet;
//import java.util.Optional;
//
//import static com.example.ssm.services.OrderInvoiceService.ORDER_ID;
//import static com.example.ssm.utils.CustomConstants.OrderEvents.FULFILL;
//import static com.example.ssm.utils.CustomConstants.OrderEvents.PAY;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//@EnableStateMachineFactory
//public class StateMachineConfiguration extends StateMachineConfigurerAdapter<CustomConstants.OrderStates, CustomConstants.OrderEvents> {
//
//
//    @Override
//    public void configure(StateMachineStateConfigurer<CustomConstants.OrderStates, CustomConstants.OrderEvents> states) throws Exception {
//        states.withStates()
//                .initial(CustomConstants.OrderStates.SUBMITTED)
//                .states(EnumSet.allOf(CustomConstants.OrderStates.class))
//                .end(CustomConstants.OrderStates.FULFILLED)
//                .end(CustomConstants.OrderStates.CANCELLED);
//    }
//
//    @Override
//    public void configure(StateMachineConfigurationConfigurer<CustomConstants.OrderStates, CustomConstants.OrderEvents> config) throws Exception {
//
//        StateMachineListenerAdapter<CustomConstants.OrderStates, CustomConstants.OrderEvents> adapter = new StateMachineListenerAdapter<>(){
//            @Override
//            public void stateChanged(State<CustomConstants.OrderStates, CustomConstants.OrderEvents> from, State<CustomConstants.OrderStates, CustomConstants.OrderEvents> to) {
//                super.stateChanged(from, to);
//                Optional.ofNullable(from).ifPresent(fromState -> {
//                    log.info("Change state from: %s to %s".formatted(fromState.getStates(),to.getStates()));
//                });
//
//            }
//        };
//
//        config.withConfiguration()
//                .autoStartup(true)
//                .listener(adapter);
//    }
//
//
//
//    @Override
//    public void configure(StateMachineTransitionConfigurer<CustomConstants.OrderStates, CustomConstants.OrderEvents> transitions) throws Exception {
//        transitions
//                .withExternal().source(CustomConstants.OrderStates.SUBMITTED).target(CustomConstants.OrderStates.PAID).event(PAY)
//                .and()
//                .withExternal().source(CustomConstants.OrderStates.PAID).target(CustomConstants.OrderStates.FULFILLED).event(CustomConstants.OrderEvents.FULFILL)
//                .and()
//                .withExternal().source(CustomConstants.OrderStates.SUBMITTED).target(CustomConstants.OrderStates.CANCELLED).event(CustomConstants.OrderEvents.CANCEl)
//                .and()
//                .withExternal().source(CustomConstants.OrderStates.PAID).target(CustomConstants.OrderStates.CANCELLED).event(CustomConstants.OrderEvents.CANCEl);
//    }
//
//    @Bean
//    public Guard<CustomConstants.OrderStates, CustomConstants.OrderEvents> guard() {
//        return ctx -> true;
//    }
//
//    @Bean
//    public Action<CustomConstants.OrderStates, CustomConstants.OrderEvents> payAction() {
//        return context -> {
//            log.info("logging-pay-action:{}", context.getEvent());
//            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PAY).setHeader(ORDER_ID, context.getMessageHeader(ORDER_ID)).build());
//        };
//    }
//
//    @Bean
//    public Action<CustomConstants.OrderStates, CustomConstants.OrderEvents> fullFillAction() {
//        return context -> {
//            log.info("logging-fullfil-action:{}", context.getEvent());
//            context.getStateMachine().sendEvent(MessageBuilder.withPayload(FULFILL).setHeader(ORDER_ID, context.getMessageHeader(ORDER_ID)).build());
//        };
//    }
//
//
//}