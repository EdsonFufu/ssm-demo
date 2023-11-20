//package com.example.ssm.services;
//
//import com.example.ssm.components.OrderInvoiceStateChangeInterceptor;
//import com.example.ssm.models.OrderInvoice;
//import com.example.ssm.repositories.OrderInvoiceRepository;
//import com.example.ssm.utils.CustomConstants;
//import lombok.Builder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.StateMachineFactory;
//import org.springframework.statemachine.support.DefaultStateMachineContext;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//@Slf4j
//@Service
//public class OrderInvoiceService {
//    private static final String PAYMENT_TYPE = "CASH_ON_DELIVERY";
//    public static final String ORDER_ID = "orderId";
//
//
//
//    @Builder
//    private record OrderResponse (long orderId, String state) {}
//
//
//    @Autowired
//    private StateMachineFactory<CustomConstants.OrderStates, CustomConstants.OrderEvents> stateMachineFactory;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//
//    @Autowired
//    private OrderInvoiceRepository orderInvoiceRepository;
//
//    @Autowired
//    private OrderInvoiceStateChangeInterceptor orderInvoiceStateChangeInterceptor;
//
//    public OrderResponse save(OrderInvoice orderInvoice) {
//        orderInvoice.setLocalDate(LocalDateTime.now().toString());
//        orderInvoice.setState(CustomConstants.OrderStates.SUBMITTED);
//        OrderInvoice newOrder = orderInvoiceRepository.save(orderInvoice);
//        log.info("Create Order From DB:[{}]", newOrder);
//        return OrderResponse.builder().orderId(newOrder.getId()).state(newOrder.getState().name()).build();
//    }
//
//    @Transactional
//    public OrderResponse pay(Long orderId) {
//       try {
//           StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> sm = build(orderId);
//           Message<CustomConstants.OrderEvents> payMessage = MessageBuilder.withPayload(CustomConstants.OrderEvents.PAY)
//                   .copyHeaders(Map.of(ORDER_ID,orderId)).build();
//           sm.sendEvent(payMessage);
//           return OrderResponse.builder().orderId(orderId).state(sm.getState().getId().name()).build();
//       }catch (Exception e){
//           log.info("Payment Failed:{}",e.getMessage());
//           e.printStackTrace();
//           throw e;
//       }
//    }
//
//
//
//    @Transactional
//    public  OrderResponse fulfill(Long orderId) {
//        try {
//        StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> sm = build(orderId);
//
//        Message<CustomConstants.OrderEvents> fulfillMessage = MessageBuilder.withPayload(CustomConstants.OrderEvents.FULFILL)
//                .copyHeaders(Map.of(ORDER_ID,orderId)).build();
//        sm.sendEvent(fulfillMessage);
//
//         return OrderResponse.builder().orderId(orderId).state(sm.getState().getId().name()).build();
//        }catch (Exception e){
//            log.info("Fullfill Failed:{}",e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    @Transactional
//    public  OrderResponse cancel(Long orderId) {
//        try {
//            StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> sm = build(orderId);
//
//            Message<CustomConstants.OrderEvents> cancelMessage = MessageBuilder.withPayload(CustomConstants.OrderEvents.CANCEl)
//                    .copyHeaders(Map.of(ORDER_ID,orderId)).build();
//            sm.sendEvent(cancelMessage);
//
//            return OrderResponse.builder().orderId(orderId).state(sm.getState().getId().name()).build();
//        }catch (Exception e){
//            log.info("Cancelled Failed:{}",e.getMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    public  StateMachine<CustomConstants.OrderStates, CustomConstants.OrderEvents> build(Long orderId){
//        OrderInvoice order = orderInvoiceRepository.getReferenceById(orderId);
//        String orderIdKey = Long.toString(order.getId());
//        var stateMachine =  stateMachineFactory.getStateMachine(orderIdKey);
//
//        stateMachine.stop();
//
//        stateMachine.getStateMachineAccessor()
//
//                .doWithAllRegions(sma -> {
//                    sma.addStateMachineInterceptor(orderInvoiceStateChangeInterceptor);
//                    sma.resetStateMachine(new DefaultStateMachineContext<>(order.getState(), null, null, null));});
//        stateMachine.start();
//        return stateMachine;
//    }
//
//}
//
//
//
//
