package org.example.authservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmBookingRequest {
    private UUID bookingId;
}
