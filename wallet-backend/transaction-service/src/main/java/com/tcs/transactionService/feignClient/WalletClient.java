package com.tcs.transactionService.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcs.transactionService.dto.WalletDto;

@FeignClient(name = "WALLET-SERVICE")
public interface WalletClient {

    @PutMapping("/wallet/debit")
    WalletDto debit(@RequestBody WalletDto dto);

    @PutMapping("/wallet/credit")
    WalletDto credit(@RequestBody WalletDto dto);

    @PutMapping("/wallet/block/{userId}")
    WalletDto block(@PathVariable String userId);
}

