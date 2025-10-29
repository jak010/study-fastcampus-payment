package com.example.fastcampus_payment

import com.example.fastcampus_payment.wallet.CreateWalletRequest
import com.example.fastcampus_payment.wallet.Wallet
import com.example.fastcampus_payment.wallet.WalletRepository
import com.example.fastcampus_payment.wallet.WalletService
import spock.lang.Specification

class WalletServiceSpockTest extends Specification {

    WalletService walletService
    WalletRepository walletRepository = Mock()

    def setup() {
        walletService = new WalletService(walletRepository)
        println "setup"
    }

    def "지갑 생상 요청 시 지갑을 갖고 있지 않다면 생성된다."() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.save() >> new Wallet(1L)

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        createdWallet != null
        createdWallet.balance == 0
    }

}
