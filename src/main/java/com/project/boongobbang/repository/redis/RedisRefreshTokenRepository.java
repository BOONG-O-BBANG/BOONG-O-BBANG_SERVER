package com.project.boongobbang.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository {
    private static final String KEY_PREFIX = "refresh-token:";

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String userNaverId, String refreshToken) {
        String key = KEY_PREFIX + userNaverId;
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    public String findRefreshToken(String userNaverId) {
        String key = KEY_PREFIX + userNaverId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String userNaverId) {
        String key = KEY_PREFIX + userNaverId;
        redisTemplate.delete(key);
    }
}
