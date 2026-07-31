package org.example.authservice.service.auth;


import jakarta.transaction.Transactional;

import org.example.authservice.entity.RefreshToken;
import org.example.authservice.entity.User;
import org.example.authservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-token.expiration-ms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(jwtUtils.generateRefreshToken(user));
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token expired. Please login again.");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(String userId) {
        UUID uuid = UUID.fromString(userId);
        refreshTokenRepository.deleteByUserId(uuid);
    }
    @Transactional
    public void deleteByRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}

