package com.example.fastcampus_payment;

import static org.mockito.Mockito.when;

import com.example.fastcampus_payment.wallet.AddBalanceWalletRequest;
import com.example.fastcampus_payment.wallet.AddBalanceWalletResponse;
import com.example.fastcampus_payment.wallet.CreateWalletRequest;
import com.example.fastcampus_payment.wallet.CreateWalletResponse;
import com.example.fastcampus_payment.wallet.Wallet;
import com.example.fastcampus_payment.wallet.WalletRepository;
import com.example.fastcampus_payment.wallet.WalletService;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import net.bytebuddy.asm.Advice.Local;
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
    @DisplayName("지갑을 조회한다. - 생성되어 있지 않은 경우")
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

    @Test
    @Transactional
    @DisplayName("지갑 잔액 추가 시 지갑이 존재하고 잔액이 충분하면 잔액이 업데이트된다.")
    void test05() {
        Long walletId = 1L;
        BigDecimal intialBalance = new BigDecimal("200.00");
        BigDecimal addAmount = new BigDecimal("100.00");
        Wallet wallet = new Wallet(
            walletId,
            walletId,
            intialBalance,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // mock
        when(walletRepository.findById(walletId))
            .thenReturn(Optional.of(wallet));

        // when
        AddBalanceWalletResponse addBalanceWalletResponse = walletService.addBalance(
            new AddBalanceWalletRequest(1L, addAmount)
        );

        // then
        Assertions.assertEquals(addBalanceWalletResponse.balance(), new BigDecimal("300.00"));
    }

    @Test
    @Transactional
    @DisplayName("지갑 잔액 추가 시, 지갑이 존재하지 않으면 예외가 발생한다.")
    void test06() {
        Long walletId = 999L;
        BigDecimal addAmount = new BigDecimal("100.00");

        // mock
        when(walletRepository.findById(walletId))
            .thenReturn(Optional.empty());

        // when && then
        Assertions.assertThrows(RuntimeException.class, () -> {
            AddBalanceWalletResponse addBalanceWalletResponse = walletService.addBalance(
                new AddBalanceWalletRequest(walletId, addAmount)
            );
        });
    }

    @Test
    @Transactional
    @DisplayName("지갑 잔액 추가 시, 잔액이 부족하면 예외가 발생한다")
    void test07() {
        Long walletId = 1L;
        BigDecimal addAmount = new BigDecimal("-101.00");
        BigDecimal initialBalance = new BigDecimal("100.00");

        Wallet wallet = new Wallet(
            walletId, walletId, initialBalance,
            LocalDateTime.now(), LocalDateTime.now()
        );

        // mock
        when(walletRepository.findById(walletId))
            .thenReturn(Optional.of(wallet));

        // when && then
        Assertions.assertThrows(RuntimeException.class, () -> {
            AddBalanceWalletResponse addBalanceWalletResponse = walletService.addBalance(
                new AddBalanceWalletRequest(walletId, addAmount)
            );
        });
    }

    @Test
    @Transactional
    @DisplayName("지갑 잔액 추가 시, 한도를 초과하면 예외가 발생한다.")
    public void test08() {
        Long walletId = 1L;
        BigDecimal addAmount = new BigDecimal(100_000_000_000L);
        BigDecimal initialBalance = new BigDecimal(100);
        Wallet wallet = new Wallet(
            walletId,
            walletId,
            initialBalance,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // mock
        when(walletRepository.findById(walletId))
            .thenReturn(Optional.of(wallet));

        // when && then
        Assertions.assertThrows(RuntimeException.class, () -> {
            walletService.addBalance(
                new AddBalanceWalletRequest(walletId, addAmount)
            );
        });


    }


}
