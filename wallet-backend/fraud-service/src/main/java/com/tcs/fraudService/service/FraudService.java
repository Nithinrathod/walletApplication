package com.tcs.fraudService.service;

import org.springframework.stereotype.Service;

import com.tcs.fraudService.bean.FraudRecord;
import com.tcs.fraudService.dto.FraudDto;
import com.tcs.fraudService.repository.FraudRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final FraudRepository fraudRepository;

    private static final String BLACKLIST_USER = "FRAUD_USER";
    private static final int MAX_FAILED_TRANSACTIONS = 3;
    private static final int MAX_TXN_PER_MINUTE = 10;

    public FraudDto check(FraudDto req) {

        FraudDto response = new FraudDto();
        response.setWalletId(req.getWalletId());
        response.setAmount(req.getAmount());

        String decision = "ALLOW";
        String reason = "No fraud detected";

        // Rule 1: Blacklisted user
        if (BLACKLIST_USER.equals(req.getWalletId())) {
            decision = "BLOCK";
            reason = "User blacklisted";
        }
        // Rule 2: High transaction amount
        else if (req.getAmount() != null && req.getAmount() > 100000) {
            decision = "BLOCK";
            reason = "High transaction amount";
        }
        // Rule 3: Too many transactions in last minute
        else if ((req.getRecentTxnCount()) > MAX_TXN_PER_MINUTE) {
            decision = "BLOCK";
            reason = "Too many transactions in last minute";
        }
        // Rule 4: Too many failed transactions
        else if ((req.getFailedTxnCount()) >= MAX_FAILED_TRANSACTIONS) {
            decision = "BLOCK";
            reason = "Too many failed transactions";
        }
        // Rule 5: Amount higher than user average
        else if (req.getAverageAmount() != null
        	      && req.getAmount() != null
        	      && req.getAmount() > 10 * req.getAverageAmount()) {
            decision = "BLOCK";
            reason = "Transaction amount higher than user average";
        }

        response.setDecision(decision);
        response.setReason(reason);
        response.setMessage("Fraud check completed");

        // Save audit record
        FraudRecord record = new FraudRecord();
        record.setWalletId(req.getWalletId());
        record.setAmount(req.getAmount());
        record.setDecision(decision);
        record.setReason(reason);

        fraudRepository.save(record);

        return response;
    }


}
