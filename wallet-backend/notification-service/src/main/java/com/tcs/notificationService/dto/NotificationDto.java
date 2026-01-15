package com.tcs.notificationService.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class NotificationDto {

    private String userId;

    private String type;      // INAPP / EMAIL
    private String title;
    @Column(length = 7000)
    private String message;

    private String status;    // SUCCESS / FAILED
}
