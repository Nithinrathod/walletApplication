package com.tcs.notificationService.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.notificationService.bean.Notification;
import com.tcs.notificationService.dto.NotificationDto;
import com.tcs.notificationService.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    
    @GetMapping("/history")
    public List<Notification> getNotifications(@RequestHeader("X-User-Id") String userId) {
        return notificationService.getUserNotifications(userId);
    }
 // In-App Notification
    @PostMapping("/inapp")
    public NotificationDto sendInApp(@RequestBody NotificationDto dto) {
        dto.setType("INAPP");
        return notificationService.sendInApp(dto);
    }

    // Email Notification
    @PostMapping("/email")
    public NotificationDto sendEmail(@RequestBody NotificationDto dto) {
        dto.setType("EMAIL");
        return notificationService.sendEmail(dto);
    }
}
