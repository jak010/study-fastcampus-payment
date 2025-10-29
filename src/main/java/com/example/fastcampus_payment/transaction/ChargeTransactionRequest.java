package com.example.fastcampus_payment.transaction;

import java.math.BigDecimal;

public record ChargeTransactionRequest(Long userId, String orderId, BigDecimal amount) {
}
