package com.tcs.notificationService.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

	@GetMapping("/auth/users/email/{userId}")
    String getEmailByUserId(@PathVariable("userId") String userId);
}

