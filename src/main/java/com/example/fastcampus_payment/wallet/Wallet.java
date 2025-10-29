package com.example.fastcampus_payment.wallet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
