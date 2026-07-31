package org.example.authservice.service.auth;


import jakarta.transaction.Transactional;
import org.example.authservice.dto.*;
import org.example.authservice.entity.PasswordChangeRequest;
import org.example.authservice.entity.User;
import org.example.authservice.entity.VerificationToken;
import org.example.authservice.enums.enums;
import org.example.authservice.kafka.producer.NotificationProducer;
import org.example.authservice.repository.PasswordChangeRequestRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.repository.VerificationTokenRepository;
import org.example.authservice.security.SecurityContextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordChangeRequestRepository passwordChangeRequestRepository;
    private final NotificationProducer notificationProducer;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, PasswordChangeRequestRepository passwordChangeRequestRepository,NotificationProducer notificationProducer) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordChangeRequestRepository = passwordChangeRequestRepository;
        this.notificationProducer = notificationProducer;
    }

    public void register(RegisterRequest user) {
        Optional<User> existing = repository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEnabled(false);

        User savedUser = repository.save(newUser);

        VerificationToken verification = new VerificationToken();

        verification.setToken(UUID.randomUUID().toString());
        verification.setUser(savedUser);
        verification.setExpiresAt(LocalDateTime.now().plusHours(24));

        verificationTokenRepository.save(verification);

        sendVerificationMail(savedUser,verification);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void verifyEmail(String token) {
        VerificationToken verification = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification token"));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token expired");
        }

        User user = verification.getUser();

        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already verified");
        }

        user.setEnabled(true);
        repository.save(user);
        verificationTokenRepository.delete(verification);
    }


    @Transactional
    public void requestPasswordChange(String oldPassword,String newPassword) {
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
        UUID userId = securityContextUtil.getCurrentUserId();

        User user = repository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect"
            );
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password must be different from the current password"
            );
        }
        passwordChangeRequestRepository.deleteByUser(user);

        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setUser(user);
        request.setNewPassword(passwordEncoder.encode(newPassword));
        request.setToken(String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000)));
        request.setExpiresAt(LocalDateTime.now().plusHours(1));

        passwordChangeRequestRepository.save(request);

        sendPasswordChangeMail(user, request);
    }

    @Transactional
    public void forgotPassword(String email) {

        Optional<User> optionalUser = repository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();
        passwordChangeRequestRepository.findByUser(user).ifPresent(passwordChangeRequestRepository::delete);

        PasswordChangeRequest request = new PasswordChangeRequest();

        request.setUser(user);
        request.setToken(String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000)));
        request.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        passwordChangeRequestRepository.save(request);

        sendForgotPasswordMail(user, request);
    }

    @Transactional
    public void resetPassword( String email, String token,String newPassword) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid email or verification token"));

        PasswordChangeRequest request = passwordChangeRequestRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.BAD_REQUEST,"Invalid email or verification token"));

        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            passwordChangeRequestRepository.delete(request);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Verification token has expired");
        }

        if (!request.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification token");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password must be different from the current password"
            );
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        passwordChangeRequestRepository.delete(request);
    }

    public void sendForgotPasswordMail(User user, PasswordChangeRequest request) {
        PasswordResetEvent event = new PasswordResetEvent();

        event.setUserId(user.getId());
        event.setEmail(user.getEmail());
        event.setFullName(user.getFullName());
        event.setToken(request.getToken());

        notificationProducer.sendPasswordResetEvent(event);
    }

    public void sendVerificationMail(User user,VerificationToken verification) {
        VerificationEvent event = new VerificationEvent();

        event.setUserId(user.getId());
        event.setEmail(user.getEmail());
        event.setFullName(user.getFullName());
        event.setToken(verification.getToken());

        notificationProducer.sendVerificationEvent(event);
    }

    @Transactional
    public void confirmPasswordChange(String code) {
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
        UUID userId = securityContextUtil.getCurrentUserId();

        User user = repository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        ));

        PasswordChangeRequest request = passwordChangeRequestRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No pending password change request"));

        if (!request.getToken().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code");
        }
        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            passwordChangeRequestRepository.delete(request);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }
        user.setPassword(request.getNewPassword());
        repository.save(user);

        passwordChangeRequestRepository.delete(request);
    }

    private void sendPasswordChangeMail(User user, PasswordChangeRequest request) {
        PasswordChangeEvent event = new PasswordChangeEvent();

        event.setUserId(user.getId());
        event.setEmail(user.getEmail());
        event.setFullName(user.getFullName());
        event.setToken(request.getToken());

        notificationProducer.sendPasswordChangeEvent(event);
    }
}

