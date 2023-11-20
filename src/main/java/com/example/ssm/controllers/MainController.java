//package com.example.ssm.controllers;
//
//import com.example.ssm.models.OrderInvoice;
//import com.example.ssm.services.OrderInvoiceService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Builder;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.function.Function;
//
//import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping
//@RestController
//public class MainController {
//
//    @Builder
//    private record Response (int statusCode, String message, Object body) {
//        Response {
//            if (body == null) {
//                body = new ObjectMapper().createObjectNode();
//            }
//        }
//    }
//
//    private final OrderInvoiceService orderInvoiceService;
//
//    private static final Function<Response,ResponseEntity<Response>> getResponse = response -> ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(response);
//
//    @PostMapping("/createOrder")
//    public ResponseEntity<Response> createOrder(@RequestBody OrderInvoice orderInvoice){
//        try{
//            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(orderInvoiceService.save(orderInvoice)).build());
//        }catch (Exception e) {
//            throw e;
//        }
//    }
//    @PostMapping("/orderPay")
//    public ResponseEntity<Response> pay(@RequestBody OrderInvoice orderInvoice) {
//        try{
//            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(orderInvoiceService.pay(orderInvoice.getId())).build());
//        }catch (Exception e) {
//            throw e;
//        }
//    }
//
//    @PostMapping("/orderFulfill")
//    public ResponseEntity<Response> fulfill(@RequestBody OrderInvoice orderInvoice) {
//        try{
//            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(orderInvoiceService.fulfill(orderInvoice.getId())).build());
//        }catch (Exception e) {
//            throw e;
//        }
//    }
//
//    @PostMapping("/orderCancel")
//    public ResponseEntity<Response> cancel(@RequestBody OrderInvoice orderInvoice) {
//        try{
//            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(orderInvoiceService.cancel(orderInvoice.getId())).build());
//        }catch (Exception e) {
//            throw e;
//        }
//    }
//
//    @ControllerAdvice
//    public static class RestResponseEntityExceptionHandler
//            extends ResponseEntityExceptionHandler {
//
//        @ExceptionHandler({ Exception.class })
//        public ResponseEntity<Response> handleException(Exception ex) {
//            log.error("Exception:[{}]",ex.toString());
//            return getResponse.apply(Response.builder().statusCode(699).message(ex.getMessage()).build());
//        }
//
//    }
//
//}
