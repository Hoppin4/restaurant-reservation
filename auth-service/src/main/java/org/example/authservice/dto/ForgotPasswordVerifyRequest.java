package org.example.authservice.dto;

import lombok.Data;

@Data
public class ForgotPasswordVerifyRequest {
    private String email;
    private String token;
}
