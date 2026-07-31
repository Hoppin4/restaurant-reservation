package org.example.authservice.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public boolean isLocked(String email) {
        String attempts = redisTemplate.opsForValue().get(getKey(email));

        return attempts != null && Integer.parseInt(attempts) >= MAX_ATTEMPTS;
    }

    public void loginFailed(String email) {

        String key = getKey(email);

        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, LOCK_DURATION);
        }
    }

    public void loginSuccess(String email) {
        redisTemplate.delete(getKey(email));
    }

    public int getAttempts(String email) {

        String value = redisTemplate.opsForValue().get(getKey(email));

        return value == null ? 0 : Integer.parseInt(value);
    }

    private String getKey(String email) {
        return "login:" + email;
    }

    public String test() {
        redisTemplate.opsForValue().set("hello", "world");
        return redisTemplate.opsForValue().get("hello");
    }
}

