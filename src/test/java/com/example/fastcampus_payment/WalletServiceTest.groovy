package com.example.fastcampus_payment

import com.example.fastcampus_payment.wallet.CreateWalletRequest
import com.example.fastcampus_payment.wallet.CreateWalletResponse
import com.example.fastcampus_payment.wallet.Wallet
import com.example.fastcampus_payment.wallet.WalletRepository
import com.example.fastcampus_payment.wallet.WalletService
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

import javax.swing.undo.AbstractUndoableEdit

@SpringBootTest
@ExtendWith(SpringExtension.class)
class WalletServiceTest {


    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;


    @Test
    @Transactional
    @DisplayName("지갑 생성 요청 시, 지갑을 갖고있지 않다면 생성된다")
    void test01() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1L);

        Assertions.assertEquals(walletRepository.findTopByUserId(1L), Optional.empty())

        // when 
        CreateWalletResponse createWalletResponse = walletService.createWallet(request);

        // then
        Assertions.assertEquals(createWalletResponse.balance(), BigDecimal.ZERO)

    }


}
