package com.example.fastcampus_payment.wallet;

import java.math.BigDecimal;

public record AddBalanceWalletRequest(Long walletId, BigDecimal amount) {

}
