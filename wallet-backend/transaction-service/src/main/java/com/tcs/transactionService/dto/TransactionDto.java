package com.tcs.transactionService.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionDto {

    private Long transactionId;

    private String senderUserId;
    private String receiverUserId;

    private Double amount;

    private String status;    // SUCCESS / FAILED / BLOCKED
    private String message;   // info message
}
