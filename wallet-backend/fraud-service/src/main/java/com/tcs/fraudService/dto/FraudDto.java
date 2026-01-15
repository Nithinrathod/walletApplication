package com.tcs.fraudService.dto;

import lombok.Data;

@Data
public class FraudDto {

    private String walletId;
    private Double amount;
    
 // passed from Transaction Service
    private Integer recentTxnCount;
    private Integer failedTxnCount;
    private Double averageAmount;

    private String decision;   // ALLOW / BLOCK
    private String reason;     // why blocked / allowed
    private String message;    // extra message if needed
}
