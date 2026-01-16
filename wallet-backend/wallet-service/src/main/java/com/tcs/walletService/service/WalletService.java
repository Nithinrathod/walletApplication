package com.tcs.walletService.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.walletService.dto.WalletDto;
import com.tcs.walletService.exception.InsufficientBalanceException;
import com.tcs.walletService.exception.WalletBlockedException;
import com.tcs.walletService.exception.WalletNotFoundException;
import com.tcs.walletService.model.Wallet;
import com.tcs.walletService.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    // Create Wallet
    public WalletDto createWallet(Wallet wallet) {

        if (walletRepository.findByUserId(wallet.getUserId()).isPresent()) {
            throw new RuntimeException("Wallet already exists for User ID: " + wallet.getUserId());
        }

        Wallet saved = walletRepository.save(wallet);

        WalletDto dto = new WalletDto();
        dto.setWalletId(saved.getWalletId());
        dto.setUserId(saved.getUserId());
        dto.setBalance(saved.getBalance());
        dto.setStatus(saved.getStatus());
        dto.setMessage("Wallet Created Successfully");
        return dto;
    }

    // Get Wallet By User Id (With Auto-Unblock Check)
    public Wallet getWallet(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("No wallet found for User: " + userId));
        
        // AUTO-UNBLOCK LOGIC
        // If wallet is BLOCKED and 10 minutes have passed
        if ("BLOCKED".equals(wallet.getStatus()) && wallet.getBlockedAt() != null) {
            if (wallet.getBlockedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
                
                // Unblock the wallet
                wallet.setStatus("ACTIVE");
                wallet.setBlockedAt(null);
                return walletRepository.save(wallet);
            }
        }
        return wallet;
    }

    // Update Wallet Status
    public WalletDto updateStatus(String userId, String newStatus) {
        Wallet wallet = getWallet(userId);

        if(wallet.getStatus().equals(newStatus)) {
            WalletDto dto = toDto(wallet);
            dto.setMessage("Wallet already in " + newStatus + " state");
            return dto;
        }

        //  HANDLE TIMESTAMP LOGIC
        if ("BLOCKED".equalsIgnoreCase(newStatus)) {
            wallet.setBlockedAt(LocalDateTime.now()); // Set timer start
        } else if ("ACTIVE".equalsIgnoreCase(newStatus)) {
            wallet.setBlockedAt(null); // Clear timer
        }

        wallet.setStatus(newStatus);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setMessage("Wallet has been " + newStatus + " successfully");
        return dto;
    }

    // Debit
    public WalletDto debit(String userId, double amount) {
        Wallet wallet = getWallet(userId); // Checks for auto-unblock first

        if ("BLOCKED".equals(wallet.getStatus())) {
            throw new WalletBlockedException("Transaction Rejected: Your wallet is blocked.");
        }

        if (wallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Transaction failed: Insufficient balance in wallet.");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setAmount(amount);
        dto.setMessage("Amount debited successfully");
        return dto;
    }

    // Credit
    public WalletDto credit(String userId, double amount) {
        Wallet wallet = getWallet(userId); // Checks for auto-unblock first

        if ("BLOCKED".equals(wallet.getStatus())) {
            throw new WalletBlockedException("Transaction Rejected: Your wallet is blocked.");
        }

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        WalletDto dto = toDto(wallet);
        dto.setAmount(amount);
        dto.setMessage("Amount credited successfully");
        return dto;
    }

    // ---------- Utility Mapper ----------
    private WalletDto toDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setWalletId(wallet.getWalletId());
        dto.setUserId(wallet.getUserId());
        dto.setBalance(wallet.getBalance());
        dto.setStatus(wallet.getStatus());
        return dto;
    }
    
    // Convert userId -> WalletDto
    public WalletDto toDto(String userId) {
        Wallet wallet = getWallet(userId);
        WalletDto dto = toDto(wallet);
        dto.setMessage("Wallet fetched successfully");
        return dto;
    }

    // Get only balance but still return WalletDto
    public WalletDto getBalanceDto(String userId) {
        Wallet wallet = getWallet(userId);
        WalletDto dto = toDto(wallet);
        dto.setMessage("Balance fetched successfully");
        return dto;
    }
}
