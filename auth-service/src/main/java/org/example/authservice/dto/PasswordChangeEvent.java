package org.example.authservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PasswordChangeEvent {
    private UUID userId;
    private String email;
    private String fullName;
    private String token;
}
