package org.example.authservice.dto;

import lombok.Data;

@Data
public class ConfirmPasswordRequest {
    private String token;
}
