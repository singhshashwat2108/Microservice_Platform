package com.queryhub.view.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache.ttl:600}")
    private long ttl;

    public long getTtl() {
        return ttl;
    }
}
