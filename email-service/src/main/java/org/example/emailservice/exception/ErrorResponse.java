package org.example.emailservice.exception;


import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime timestamp) {
}
