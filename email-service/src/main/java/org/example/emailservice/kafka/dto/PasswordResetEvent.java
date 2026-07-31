package org.example.emailservice.kafka.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PasswordResetEvent {
    private UUID userId;
    private String email;
    private String fullName;
    private String token;
}
