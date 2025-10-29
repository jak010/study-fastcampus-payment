package com.example.fastcampus_payment.transaction;

import java.math.BigDecimal;

public record PaymentTransactionResponse(Long walletId, BigDecimal balance) {
}
