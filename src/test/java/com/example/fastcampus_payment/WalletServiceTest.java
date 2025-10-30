package com.example.fastcampus_payment;

import static org.mockito.Mockito.when;

import com.example.fastcampus_payment.wallet.CreateWalletRequest;
import com.example.fastcampus_payment.wallet.CreateWalletResponse;
import com.example.fastcampus_payment.wallet.Wallet;
import com.example.fastcampus_payment.wallet.WalletRepository;
import com.example.fastcampus_payment.wallet.WalletService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class WalletServiceTest {


    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;


    @Test
    @Transactional
    @DisplayName("지갑 생성 요청 시, 지갑을 갖고있지 않다면 생성된다")
    void test01() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1L);

        Assertions.assertEquals(walletRepository.findTopByUserId(1L), Optional.empty());

        // when 
        CreateWalletResponse createWalletResponse = walletService.createWallet(request);

        // then
        Assertions.assertEquals(createWalletResponse.balance(), BigDecimal.ZERO);

    }

    @Test
    @Transactional
    @DisplayName("지갑 생성 요청 시, 지갑을 이미 갖고 있다면 오류를 응답한다.")
    void test02() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1L);

        when(walletRepository.findTopByUserId(1L))
            .thenReturn(Optional.of(new Wallet(1L)));

        // when && then
        Assertions.assertThrows(RuntimeException.class, () -> {
            walletService.createWallet(request);
        });

    }

    @Test
    @Transactional
    @DisplayName("지갑을 조회한다. - 생성되어있는 경우")
    void test03() {
        // given
        Wallet wallet = new Wallet(1L);
        wallet.setBalance(new BigDecimal(1000));

        // when
        when(walletRepository.findTopByUserId(1L))
            .thenReturn(Optional.of(wallet));

        Optional<Wallet> result = walletRepository.findTopByUserId(1L);

        // then

        Assertions.assertTrue(result.isPresent());  // 값이 존재하는지 확인
        Assertions.assertEquals(new BigDecimal(1000), result.get().getBalance());  // balance 확인
        Assertions.assertEquals(1L, result.get().getUserId());  // userId 확인
    }

    @Test
    @Transactional
    @DisplayName("지갑을 조회한다. - 생성되어있지 않은 경우")
    void test04() {
        // given
        Wallet wallet = new Wallet(1L);
        wallet.setBalance(new BigDecimal(1000));

        // when
        when(walletRepository.findTopByUserId(1L))
            .thenReturn(Optional.empty());

        Optional<Wallet> result = walletRepository.findTopByUserId(1L);

        // then
        Assertions.assertTrue(result.isEmpty());  // 값이 존재하는지 확인
        
    }


}
