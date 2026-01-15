package com.tcs.transactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private Long walletId;
    private String userId;
    private Double amount;     // for debit / credit request
    private Double balance;    // for responses
    private String status;     // ACTIVE / BLOCKED / SUCCESS / FAILED
    private String message;    // extra info
}
