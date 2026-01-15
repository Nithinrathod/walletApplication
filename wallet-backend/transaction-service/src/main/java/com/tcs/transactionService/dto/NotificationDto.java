package com.tcs.transactionService.dto;

import lombok.Data;

@Data
public class NotificationDto {

    private String userId;

    private String type;      // INAPP / EMAIL
    private String title;
    private String message;

    private String status;    // SUCCESS / FAILED
}
