package org.example.authservice.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.authservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;


    public String generateAccessToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret));
    }


    public String generateRefreshToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret));
    }
}
