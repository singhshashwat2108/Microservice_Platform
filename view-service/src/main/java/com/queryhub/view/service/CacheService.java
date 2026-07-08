package com.queryhub.view.service;

import com.queryhub.view.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheConfig cacheConfig;

    public CacheService(RedisTemplate<String, Object> redisTemplate, CacheConfig cacheConfig) {
        this.redisTemplate = redisTemplate;
        this.cacheConfig = cacheConfig;
    }

    public Object get(String key) {
        try {
            Object val = redisTemplate.opsForValue().get(key);
            if (val != null) {
                log.info("Cache HIT for key={}", key);
            } else {
                log.info("Cache MISS for key={}", key);
            }
            return val;
        } catch (Exception e) {
            log.error("Failed to read from cache for key={} (Graceful fallback to source database)", key, e);
            return null;
        }
    }

    public void put(String key, Object value) {
        if (value == null) return;
        try {
            long ttlSeconds = cacheConfig.getTtl();
            log.info("Caching key={} with TTL={}s", key, ttlSeconds);
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
        } catch (Exception e) {
            log.error("Failed to write to cache for key={}", key, e);
        }
    }
}
