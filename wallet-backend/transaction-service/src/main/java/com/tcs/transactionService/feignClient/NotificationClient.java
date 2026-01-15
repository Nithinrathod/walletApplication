package com.tcs.transactionService.feignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcs.transactionService.dto.NotificationDto;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

	@PostMapping("/notification/inapp")
    NotificationDto sendInApp(@RequestBody NotificationDto dto);

    @PostMapping("/notification/email")
    NotificationDto sendEmail(@RequestBody NotificationDto dto);
}
