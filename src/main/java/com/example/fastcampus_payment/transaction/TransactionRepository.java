package com.example.fastcampus_payment.transaction;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findTransactionByOrderId(String orderId);

}
