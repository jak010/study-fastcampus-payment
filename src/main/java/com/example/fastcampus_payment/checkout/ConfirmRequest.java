package com.example.fastcampus_payment.checkout;

public record ConfirmRequest(
    String paymentKey, String orderId, String amount
) {

}
