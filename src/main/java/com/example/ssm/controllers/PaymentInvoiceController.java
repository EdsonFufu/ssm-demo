package com.example.ssm.controllers;

import com.example.ssm.models.PaymentInvoice;
import com.example.ssm.services.PaymentInvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("invoice")
@RestController
public class PaymentInvoiceController {

    @Builder
    private record Response (int statusCode, String message, Object body) {
        Response {
            if (body == null) {
                body = new ObjectMapper().createObjectNode();
            }
        }
    }

    private final PaymentInvoiceService paymentInvoiceService;

    private static final Function<Response,ResponseEntity<Response>> getResponse = response -> ResponseEntity.ok().header(CONTENT_TYPE,APPLICATION_JSON_VALUE).body(response);

    @PostMapping("/request")
    public ResponseEntity<Response> createOrder(@RequestBody PaymentInvoice paymentInvoice){
        try{
            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(paymentInvoiceService.newPayment(paymentInvoice)).build());
        }catch (Exception e) {
            throw e;
        }
    }
    @PostMapping("/screen/{invoiceId}")
    public ResponseEntity<Response> screen(@PathVariable(name = "invoiceId") Long invoiceId) {
        try{
            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(paymentInvoiceService.screen(invoiceId)).build());
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/confirm/{invoiceId}")
    public ResponseEntity<Response> confirm(@PathVariable(name = "invoiceId") Long invoiceId) {
        try{
            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(paymentInvoiceService.confirm(invoiceId)).build());
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/cancel/{invoiceId}")
    public ResponseEntity<Response> cancel(@PathVariable(name = "invoiceId") Long invoiceId) {
        try{
            return getResponse.apply(Response.builder().statusCode(600).message("Success").body(paymentInvoiceService.cancel(invoiceId)).build());
        }catch (Exception e) {
            throw e;
        }
    }

    @ControllerAdvice
    public static class RestResponseEntityExceptionHandler
            extends ResponseEntityExceptionHandler {

        @ExceptionHandler({ Exception.class })
        public ResponseEntity<Response> handleException(Exception ex) {
            log.error("Exception:[{}]",ex.toString());
            return getResponse.apply(Response.builder().statusCode(699).message(ex.getMessage()).build());
        }

    }

}
