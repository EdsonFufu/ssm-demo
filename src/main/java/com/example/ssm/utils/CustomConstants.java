package com.example.ssm.utils;

public class CustomConstants {
    private CustomConstants() {
    }

    public enum OrderStates {
        SUBMITTED, PAID, FULFILLED, CANCELLED
    }
    public enum OrderEvents {
        FULFILL, PAY,  CANCEl
    }
}
