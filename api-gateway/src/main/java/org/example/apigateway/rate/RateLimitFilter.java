package org.example.apigateway.rate;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Component
public class RateLimitFilter implements GlobalFilter {
    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String endpoint = exchange.getRequest().getURI().getPath();
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        String key;

        int limit;

        Duration duration = Duration.ofMinutes(1);

        if(endpoint.equals("/auth/login")) {
            key = "rate:ip:" + ip + ":" + endpoint;
            limit = 5;
        } else if(endpoint.equals("/auth/register")) {
            key = "rate:ip:" + ip + ":" + endpoint;
            limit = 3;
        } else {
            if(userId != null) {
                key = "rate:user:" + userId + ":" + endpoint;
            } else {
                key = "rate:ip:" + ip + ":" + endpoint;
            }
            limit = 100;
        }

        if(!rateLimitService.allow(key, limit, duration)) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}