package org.example.apigateway.rate;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allow(String key, int limit, Duration duration) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null) {
            return false;
        }
        if (count == 1) {
            redisTemplate.expire(key, duration);
        }
        return count <= limit;
    }
}
