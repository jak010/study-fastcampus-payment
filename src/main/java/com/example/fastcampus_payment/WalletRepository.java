package com.example.fastcampus_payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
