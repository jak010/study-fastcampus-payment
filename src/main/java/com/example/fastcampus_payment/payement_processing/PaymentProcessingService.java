package com.example.fastcampus_payment.payement_processing;


import com.example.fastcampus_payment.external.PaymentGatewayService;
import com.example.fastcampus_payment.order.Order;
import com.example.fastcampus_payment.order.Order.Status;
import com.example.fastcampus_payment.order.OrderRepository;
import com.example.fastcampus_payment.retry.ConfirmRequest;
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
