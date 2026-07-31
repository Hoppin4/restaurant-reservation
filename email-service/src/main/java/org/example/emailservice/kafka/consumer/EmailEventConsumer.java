package org.example.emailservice.kafka.consumer;

import org.example.emailservice.kafka.dto.PasswordChangeEvent;
import org.example.emailservice.kafka.dto.PasswordResetEvent;
import org.example.emailservice.kafka.dto.VerificationEvent;
import org.example.emailservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailEventConsumer {
    private final EmailService emailService;

    public EmailEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }


    @KafkaListener(topics = "auth.user.password-reset.requested", groupId = "email-service")
    public void consumePasswordResetEvent(PasswordResetEvent event){
        emailService.sendPasswordReset(event);
    }

    @KafkaListener(topics = "auth.user.verification.requested", groupId = "email-service")
    public void consumeVerificationEvent(VerificationEvent event){
        emailService.sendVerificationToken(event);
    }

    @KafkaListener(topics = "auth.user.password-change.requested", groupId = "email-service")
    public void consumePasswordChangeEvent(PasswordChangeEvent event){
        emailService.sendPasswordChange(event);
    }
}
