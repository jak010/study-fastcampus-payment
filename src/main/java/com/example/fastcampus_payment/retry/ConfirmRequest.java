package com.example.fastcampus_payment.retry;

public record ConfirmRequest(String paymentKey, String orderId, String amount) {

}
