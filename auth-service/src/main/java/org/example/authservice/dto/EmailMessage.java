package org.example.authservice.dto;

import lombok.Data;
import org.example.authservice.enums.enums;

@Data
public class EmailMessage {
    private enums.EmailType type;
    private String to;
    private String subject;

    // Verification
    private String fullName;
    private String token;

    private String body;

    // Booking
    private String qrToken;
}
