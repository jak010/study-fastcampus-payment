package com.example.fastcampus_payment;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;


    // CRUD Operation

    @Transactional
    public Wallet createWallet(CreateWalletRequest request) {
        return walletRepository.save(new Wallet(request.userId()));
    }

//    public Wallet saveWallet()

}
