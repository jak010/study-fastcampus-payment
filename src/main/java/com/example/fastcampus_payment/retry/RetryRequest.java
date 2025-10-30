package com.example.fastcampus_payment.retry;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.apache.catalina.users.GenericRole;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RetryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestJson;
    private String requestId;
    private Integer retryCount;
    private String errorResponse;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RetryRequest(String requestJson, String requestId,
        String errorResponse, Type type
    ) {
        this.requestJson = requestJson;
        this.requestId = requestId;
        this.errorResponse = errorResponse;
        this.status = Status.IN_PROGRESS;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        IN_PROGRESS, SUCCESS, FAILURE
    }

    public enum Type {
        CONFIRM
    }

}
