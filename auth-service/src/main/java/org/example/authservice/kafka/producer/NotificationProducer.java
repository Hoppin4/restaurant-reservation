package org.example.authservice.kafka.producer;

import org.example.authservice.dto.PasswordChangeEvent;
import org.example.authservice.dto.PasswordResetEvent;
import org.example.authservice.dto.VerificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    public static final String PASSWORD_RESET = "auth.user.password-reset.requested";
    public static final String PASSWORD_CHANGE = "auth.user.password-change.requested";
    public static final String USER_VERIFICATION = "auth.user.verification.requested";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPasswordResetEvent(PasswordResetEvent event) {
        kafkaTemplate.send(PASSWORD_RESET, event);
    }

    public void sendVerificationEvent(VerificationEvent event) {
        kafkaTemplate.send(USER_VERIFICATION, event);
    }

    public void sendPasswordChangeEvent(PasswordChangeEvent event) {
        kafkaTemplate.send(PASSWORD_CHANGE, event);
    }
}
