package org.example.apigateway.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String header = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = header.substring(7);

        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);

            String userId = decodedJWT.getClaim("userId").asString();
            String role = decodedJWT.getClaim("role").asString();

            ServerHttpRequest request = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .build();

            return chain.filter(exchange.mutate().request(request).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }
}