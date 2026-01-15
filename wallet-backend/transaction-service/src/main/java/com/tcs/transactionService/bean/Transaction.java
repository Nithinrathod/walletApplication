package com.tcs.transactionService.bean;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private String senderUserId;
    private String receiverUserId;

    private Double amount;

    private String status;     // SUCCESS / FAILED / BLOCKED
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
}