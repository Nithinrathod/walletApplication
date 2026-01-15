package com.tcs.walletService.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // This makes it return 403 automatically
public class WalletBlockedException extends RuntimeException {
    public WalletBlockedException(String message) {
        super(message);
    }
}