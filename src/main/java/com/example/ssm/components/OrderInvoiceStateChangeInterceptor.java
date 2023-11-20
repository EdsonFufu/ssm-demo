//package com.example.ssm.components;
//
//import com.example.ssm.models.OrderInvoice;
//import com.example.ssm.repositories.OrderInvoiceRepository;
//import com.example.ssm.utils.CustomConstants;
//import org.springframework.messaging.Message;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.state.State;
//import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
//import org.springframework.statemachine.transition.Transition;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//import java.util.Optional;
//
//import static com.example.ssm.services.OrderInvoiceService.ORDER_ID;
//
//
//@Component
//public class OrderInvoiceStateChangeInterceptor extends StateMachineInterceptorAdapter<CustomConstants.OrderStates, CustomConstants.OrderEvents> {
//    private final OrderInvoiceRepository orderInvoiceRepository;
//
//    public OrderInvoiceStateChangeInterceptor(OrderInvoiceRepository orderInvoiceRepository) {
//        this.orderInvoiceRepository = orderInvoiceRepository;
//    }
//
//
//    @Override
//    public void preStateChange(State<CustomConstants.OrderStates, CustomConstants.OrderEvents> state, Message<CustomConstants.OrderEvents> message, Transition<CustomConstants.OrderStates, CustomConstants.OrderEvents> transition, StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> stateMachine, StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> rootStateMachine) {
////        super.preStateChange(state, message, transition, stateMachine, rootStateMachine);
//        Optional.ofNullable(message).flatMap(msg -> Optional.ofNullable(Objects.toString(msg.getHeaders().get(ORDER_ID),"-1L"))).ifPresent(paymentId -> {
//            OrderInvoice payment = orderInvoiceRepository.getReferenceById(Long.valueOf(paymentId));
//            System.out.println("PreStateChange:[" +payment+"] => new State[" + state.getId() +"]");
//            payment.setState(state.getId());
//            System.out.println("Invoice After Change State:["+payment+"] => new State[" + state.getId() +"]");
//            OrderInvoice paymentSaved = orderInvoiceRepository.save(payment);
//            System.out.println("Invoice After Save:["+paymentSaved+"]");
//        });
//    }
//}
