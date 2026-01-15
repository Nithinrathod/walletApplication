package com.tcs.transactionService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.transactionService.bean.Transaction;
import com.tcs.transactionService.dto.FraudDto;
import com.tcs.transactionService.dto.TransactionDto;
import com.tcs.transactionService.dto.WalletDto;
import com.tcs.transactionService.feignClient.FraudClient;
import com.tcs.transactionService.feignClient.WalletClient;
import com.tcs.transactionService.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletClient walletClient;
    private final FraudClient fraudClient;
    private final TransactionRepository transactionRepo;
    
    //  Inject the new Async Service instead of NotificationClient
    private final AsyncNotificationService asyncNotificationService;

    // ---------------- ADD MONEY ----------------
    public TransactionDto addMoney(TransactionDto req) {

        try {
            WalletDto walletReq = new WalletDto();
            walletReq.setUserId(req.getReceiverUserId());
            walletReq.setAmount(req.getAmount());

            WalletDto walletResponse = walletClient.credit(walletReq);

            //  This call is now non-blocking (Fire-and-Forget)
            sendNotifications(
                req.getReceiverUserId(),
                "Money Added Successfully",
                "₹" + req.getAmount() + " added. New Balance = " + walletResponse.getBalance()
            );

            req.setStatus("SUCCESS");
            req.setMessage("Money added successfully");

        } catch (Exception e) {
            sendNotifications(
                req.getReceiverUserId(),
                "Add Money Failed",
                "Unable to add money"
            );

            req.setStatus("FAILED");
            req.setMessage(e.getMessage());
        }

        Transaction saved = saveTransaction(req);
        req.setTransactionId(saved.getTransactionId());
        return req;
    }

    // ---------------- TRANSFER ----------------
    public TransactionDto transfer(TransactionDto req) {

        //  BUILD FRAUD REQUEST
        FraudDto fraudReq = new FraudDto();
        fraudReq.setWalletId(req.getSenderUserId());
        fraudReq.setAmount(req.getAmount());

        fraudReq.setRecentTxnCount(
            transactionRepo.countRecentTransactions(
                req.getSenderUserId(),
                LocalDateTime.now().minusMinutes(1)
            )
        );

        fraudReq.setFailedTxnCount(
            transactionRepo.countFailedTransactions(
                req.getSenderUserId(),
                LocalDateTime.now().minusHours(24)
            )
        );

        fraudReq.setAverageAmount(
            transactionRepo.findAverageAmount(req.getSenderUserId())
        );

        FraudDto fraudResponse = fraudClient.check(fraudReq);

        //  FRAUD BLOCKED
        if ("BLOCK".equalsIgnoreCase(fraudResponse.getDecision())) {

            walletClient.block(req.getSenderUserId());

            sendNotifications(
                req.getSenderUserId(),
                "Transaction Blocked",
                fraudResponse.getReason()
            );

            req.setStatus("BLOCKED");
            req.setMessage(fraudResponse.getReason());

            Transaction saved = saveTransaction(req);
            req.setTransactionId(saved.getTransactionId());
            return req;
        }

        //  FRAUD ALLOWED
        try {
            WalletDto debitReq = new WalletDto();
            debitReq.setUserId(req.getSenderUserId());
            debitReq.setAmount(req.getAmount());
            walletClient.debit(debitReq);

            WalletDto creditReq = new WalletDto();
            creditReq.setUserId(req.getReceiverUserId());
            creditReq.setAmount(req.getAmount());
            walletClient.credit(creditReq);

            //  Send Async Notifications
            sendNotifications(
                req.getSenderUserId(),
                "Amount Debited",
                "₹" + req.getAmount() + " sent to " + req.getReceiverUserId()
            );

            sendNotifications(
                req.getReceiverUserId(),
                "Amount Credited",
                "₹" + req.getAmount() + " received from " + req.getSenderUserId()
            );

            req.setStatus("SUCCESS");
            req.setMessage("Transfer successful");

        } catch (Exception e) {
            sendNotifications(
                req.getSenderUserId(),
                "Transaction Failed",
                "System error occurred"
            );

            req.setStatus("FAILED");
            req.setMessage(e.getMessage());
        }

        Transaction saved = saveTransaction(req);
        req.setTransactionId(saved.getTransactionId());
        return req;
    }
    
    public List<Transaction> getHistory(String userId) {
        return transactionRepo.findBySenderUserIdOrReceiverUserIdOrderByCreatedAtDesc(userId, userId);
    }
    // ---------------- SAVE TRANSACTION ----------------
    private Transaction saveTransaction(TransactionDto req) {
        Transaction tx = new Transaction();
        tx.setSenderUserId(req.getSenderUserId());
        tx.setReceiverUserId(req.getReceiverUserId());
        tx.setAmount(req.getAmount());
        tx.setStatus(req.getStatus());
        tx.setMessage(req.getMessage());
        tx.setCreatedAt(LocalDateTime.now());
        return transactionRepo.save(tx);
    }

    // ---------------- NOTIFICATIONS ----------------
    private void sendNotifications(String userId, String title, String message) {
        // Delegate to the Async Service
        // This method returns immediately, free up the Transaction Service
        asyncNotificationService.sendNotification(userId, title, message);
    }
}
