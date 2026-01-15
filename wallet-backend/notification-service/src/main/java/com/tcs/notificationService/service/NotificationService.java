package com.tcs.notificationService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.notificationService.bean.Notification;
import com.tcs.notificationService.dto.NotificationDto;
import com.tcs.notificationService.feignClient.AuthClient;
import com.tcs.notificationService.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AuthClient authClient;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    // ---------------- IN-APP NOTIFICATION ----------------
    public NotificationDto sendInApp(NotificationDto dto) {

        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setType("INAPP");
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());

        notificationRepository.save(notification);

        dto.setStatus("SUCCESS");
        return dto;
    }

    // ---------------- EMAIL NOTIFICATION ----------------
    public NotificationDto sendEmail(NotificationDto dto) {
        // 1 Resolve actual email from Auth Service using the UUID userId
        String resolvedEmail = authClient.getEmailByUserId(dto.getUserId());

        // 2 Persist notification record
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setType("EMAIL");
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notificationRepository.save(notification);

        // 3 Send email to the resolved address
        emailService.sendEmail(
                resolvedEmail,
                dto.getTitle(),
                dto.getMessage()
        );

        dto.setStatus("SUCCESS");
        return dto;
    }
}
