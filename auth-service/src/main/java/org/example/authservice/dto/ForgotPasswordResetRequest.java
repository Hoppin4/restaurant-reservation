package org.example.authservice.dto;

import lombok.Data;

@Data
public class ForgotPasswordResetRequest {
    private String email;
    private String token;
    private String newPassword;
}
