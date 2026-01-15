package com.tcs.notificationService.bean;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String type;        // INAPP / EMAIL
    private String title;
    
    @Column(length = 5000)
    private String message;

    private LocalDateTime sentAt = LocalDateTime.now();
}
