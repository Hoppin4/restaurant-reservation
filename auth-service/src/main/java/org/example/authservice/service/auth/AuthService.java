package org.example.authservice.service.auth;

import org.example.authservice.entity.User;
import org.example.authservice.entity.VerificationToken;
import org.example.authservice.repository.VerificationTokenRepository;
import org.example.authservice.service.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final VerificationTokenRepository verificationTokenRepository;


    public AuthService(UserService userService, PasswordEncoder passwordEncoder, RedisService redisService, VerificationTokenRepository verificationTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public User login(String email, String rawPassword) {

        User user = userService.findByEmail(email);

        if (redisService.isLocked(email)) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Too many failed login attempts. Please try again in 15 minutes.");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            redisService.loginFailed(email);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid email or password");
        }

        if(!user.isEnabled()){
            VerificationToken verification = new VerificationToken();

            verification.setToken(UUID.randomUUID().toString());
            verification.setUser(user);
            verification.setExpiresAt(LocalDateTime.now().plusHours(24));

            verificationTokenRepository.save(verification);

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is not enabled, please check your email for verification link.");
        }

        redisService.loginSuccess(email);
        return user;
    }
}
