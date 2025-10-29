package com.example.fastcampus_payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Wallet {

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private BigDecimal balance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wallet(Long userId) {
        this.userId = userId;
        this.balance = new BigDecimal(0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

}
