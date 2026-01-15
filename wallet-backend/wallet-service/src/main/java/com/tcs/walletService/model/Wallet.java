package com.tcs.walletService.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    @Column(unique = true, nullable = false)
    private String userId;
    private double balance;
    private String status; //ACTIVE OR BLOCKED
    private LocalDateTime blockedAt;
}