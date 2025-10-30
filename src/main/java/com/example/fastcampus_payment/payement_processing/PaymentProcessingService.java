package com.example.fastcampus_payment.payement_processing;


import com.example.fastcampus_payment.checkout.ConfirmRequest;
import com.example.fastcampus_payment.external.PaymentGatewayService;
import com.example.fastcampus_payment.order.Order;
import com.example.fastcampus_payment.order.Order.Status;
import com.example.fastcampus_payment.order.OrderRepository;
import com.example.fastcampus_payment.retry.RetryRequest;
import com.example.fastcampus_payment.retry.RetryRequest.Type;
import com.example.fastcampus_payment.retry.RetryRequestRepository;
import com.example.fastcampus_payment.transaction.ChargeTransactionRequest;
import com.example.fastcampus_payment.transaction.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentProcessingService {

    private final PaymentGatewayService paymentGatewayService;
    private final TransactionService transactionService;

    private final OrderRepository orderRepository;

    private final RetryRequestRepository retryRequestRepository;
    private final ObjectMapper objectMapper;

    public void createPayment(ConfirmRequest confirmRequest) {
         /*
        3. 결제서비스 > PG 승인 요청
        4. 결제서비스 > 결제기록 저장
            > 결제수단으로 바로 결제하는 메서드 구현
        ...
        6. 주문서비스에 응답
        7. 주문을 APPROVED 상태로 변경
         */

        paymentGatewayService.confirm(confirmRequest);
        transactionService.pgPayment();

        approveOrder(confirmRequest.orderId());
    }


    public void createCharge(ConfirmRequest confirmRequest, boolean isRetry) {

        try {
            paymentGatewayService.confirm(confirmRequest);

        } catch (Exception e) {
            log.error("caught exception on createCharge", e);

            if (!isRetry && e instanceof RestClientException
                && e.getCause() instanceof SocketTimeoutException) {
                createRetryRequest(confirmRequest, e);
            }
            throw e;
        }

        final Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        transactionService.charge(
            new ChargeTransactionRequest(
                order.getUserId(),
                confirmRequest.orderId(),
                new BigDecimal(confirmRequest.amount())
            )

        );

        approveOrder(confirmRequest.orderId());

    }


    @SneakyThrows
    public void createRetryRequest(ConfirmRequest confirmRequest, Exception e) {
        RetryRequest retryRequest = new RetryRequest(
            objectMapper.writeValueAsString(confirmRequest),
            confirmRequest.orderId(),
            e.getMessage(),
            Type.CONFIRM
        );
        retryRequestRepository.save(retryRequest);
    }

    private void approveOrder(String orderId) {
        final Order order = orderRepository.findByRequestId(orderId);
        order.setStatus(Status.APPROVED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

    }

}
