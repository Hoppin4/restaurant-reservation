package org.example.apigateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    public DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm()).build();
        return verifier.verify(token);
    }
}

