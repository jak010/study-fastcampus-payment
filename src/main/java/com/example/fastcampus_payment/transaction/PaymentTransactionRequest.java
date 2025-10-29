package com.example.fastcampus_payment.transaction;

import java.math.BigDecimal;

public record PaymentTransactionRequest(Long walletId, String courseId, BigDecimal amount) {

}