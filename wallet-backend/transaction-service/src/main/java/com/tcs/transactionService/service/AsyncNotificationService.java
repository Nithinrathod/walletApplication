package com.tcs.transactionService.service;

import com.tcs.transactionService.dto.NotificationDto;
import com.tcs.transactionService.feignClient.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncNotificationService {

    private final NotificationClient notificationClient;

    //  Run this in a background thread
    @Async("taskExecutor") // Uses the thread pool we defined earlier
    public void sendNotification(String userId, String title, String message) {
        try {
            System.out.println("Async thread started: Sending notifications for " + userId);

            // 1. Send In-App Notification
            NotificationDto inApp = new NotificationDto();
            inApp.setUserId(userId);
            inApp.setType("INAPP");
            inApp.setTitle(title);
            inApp.setMessage(message);
            notificationClient.sendInApp(inApp);

            // 2. Send Email Notification
            NotificationDto email = new NotificationDto();
            email.setUserId(userId);
            email.setType("EMAIL");
            email.setTitle(title);
            email.setMessage(message);
            notificationClient.sendEmail(email);

            System.out.println("Async thread finished: Notifications sent.");

        } catch (Exception e) {
            // Log error but do not fail the transaction
            System.err.println("Failed to send async notification: " + e.getMessage());
        }
    }
}