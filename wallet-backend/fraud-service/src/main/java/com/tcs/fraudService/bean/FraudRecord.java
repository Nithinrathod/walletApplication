package com.tcs.fraudService.bean;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FraudRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String walletId;
    private Double amount;

    private String decision;   // ALLOW / BLOCK
    private String reason;

    private LocalDateTime checkedAt = LocalDateTime.now();
}
