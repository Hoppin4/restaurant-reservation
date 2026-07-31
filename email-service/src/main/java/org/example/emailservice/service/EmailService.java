package org.example.emailservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.emailservice.exception.EmailSendException;
import org.example.emailservice.kafka.dto.PasswordChangeEvent;
import org.example.emailservice.kafka.dto.PasswordResetEvent;
import org.example.emailservice.kafka.dto.VerificationEvent;
import org.example.emailservice.template.EventTemplates;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EventTemplates eventTemplates;

    public EmailService(JavaMailSender mailSender, EventTemplates eventTemplates) {
        this.mailSender = mailSender;
        this.eventTemplates = eventTemplates;
    }

    public void sendPasswordReset(PasswordResetEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Reset Your Password");

            String body = eventTemplates.passwordResetBody(event.getFullName(), event.getToken());
            helper.setText(body, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException("Password reset mail could not be sent", e);
        }
    }

    public void sendVerificationToken(VerificationEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Verification Code");

            String body = eventTemplates.verificationBody(event.getFullName(), event.getToken());

            helper.setText(body, false);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Verification mail could not be sent", e);
        }
    }

    public void sendPasswordChange(PasswordChangeEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Change Your Password");

            String body = eventTemplates.passwordChangeBody(event.getFullName(), event.getToken());

            helper.setText(body, false);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Password change mail could not be sent", e);
        }
    }

}
