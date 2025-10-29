package com.example.fastcampus_payment.transaction;


import com.example.fastcampus_payment.wallet.AddBalanceWalletRequest;
import com.example.fastcampus_payment.wallet.AddBalanceWalletResponse;
import com.example.fastcampus_payment.wallet.FindWalletResponse;
import com.example.fastcampus_payment.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ChargeTransactionResponse charge(ChargeTransactionRequest request) {
        final FindWalletResponse findWalletResponse = walletService.findWalletByUserId(request.userId());

        if (findWalletResponse == null) {
            throw new RuntimeException("사용자 지갑이 존재하지 않습니다.");
        }

        if (transactionRepository.findTransactionByOrderId(request.orderId()).isPresent()) {
            throw new RuntimeException("이미 충전된 거래입니다.");
        }

        final AddBalanceWalletResponse wallet = walletService.addBalance(
            new AddBalanceWalletRequest(findWalletResponse.id(), request.amount()));
        final Transaction transaction = Transaction.createChargeTransaction(request.userId(), wallet.id(),
            request.orderId(), request.amount());
        transactionRepository.save(transaction);
        return new ChargeTransactionResponse(wallet.id(), wallet.balance());


    }


}
