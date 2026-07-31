package org.example.authservice.enums;

public class enums {
    public enum Role {
        USER, ADMIN,OWNER
    }
    public enum EmailType {
        FORGOT_PASSWORD,
        VERIFY_EMAIL,
        BOOKING_CONFIRMED,
        PASSWORD_RESET,
        PASSWORD_CHANGE
    }
}
