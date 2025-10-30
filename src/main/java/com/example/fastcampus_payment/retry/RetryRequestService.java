package com.example.fastcampus_payment.retry;

import com.example.fastcampus_payment.payement_processing.PaymentProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RetryRequestService {

    private final RetryRequestRepository retryRequestRepository;


    private final PaymentProcessingService paymentProcessingService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void retry(Long retryRequestId) {
        final RetryRequest request = retryRequestRepository
            .findById(retryRequestId)
            .orElseThrow();

        // TODO 정책 확장
        final ConfirmRequest confirmRequest = objectMapper.readValue(
            request.getRequestJson(), ConfirmRequest.class
        );

        try {
            paymentProcessingService.createCharge(confirmRequest, true);
            request.setStatus(RetryRequest.Status.SUCCESS);
        } catch (Exception e) {
            request.setRetryCount(request.getRetryCount() + 1);
        } finally {
            request.setUpdatedAt(LocalDateTime.now());
            retryRequestRepository.save(request);
        }


    }
}
