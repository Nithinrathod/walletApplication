package com.tcs.transactionService.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader; // Import this
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.transactionService.bean.Transaction;
import com.tcs.transactionService.dto.TransactionDto;
import com.tcs.transactionService.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    

    @GetMapping("/history")
    public List<Transaction> getHistory(@RequestHeader("X-User-Id") String userId) {
        return transactionService.getHistory(userId);
    }

    @PostMapping("/addMoney")
    public TransactionDto addMoney(@RequestBody TransactionDto dto, @RequestHeader("X-User-Id") String userId) {
        // Security: Ensure money is added TO the logged-in user's wallet
        dto.setReceiverUserId(userId); 
        dto.setSenderUserId(userId); // Self-funding source
        return transactionService.addMoney(dto);
    }

    @PostMapping("/transfer")
    public TransactionDto transfer(@RequestBody TransactionDto dto, @RequestHeader("X-User-Id") String userId) {
        // Security: Ensure the sender IS the logged-in user
        dto.setSenderUserId(userId); 
        return transactionService.transfer(dto);
    }
}