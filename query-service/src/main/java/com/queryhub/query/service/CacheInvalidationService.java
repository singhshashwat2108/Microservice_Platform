package com.queryhub.query.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Set;

@Service
public class CacheInvalidationService {

    private static final Logger log = LoggerFactory.getLogger(CacheInvalidationService.class);
    private final StringRedisTemplate redisTemplate;

    public CacheInvalidationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void invalidate(Long queryId, String categoryName) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            log.info("Registering cache invalidation after transaction commit for queryId={}, categoryName={}", queryId, categoryName);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    performInvalidation(queryId, categoryName);
                }
            });
        } else {
            log.info("No active transaction, performing cache invalidation immediately for queryId={}, categoryName={}", queryId, categoryName);
            performInvalidation(queryId, categoryName);
        }
    }

    private void performInvalidation(Long queryId, String categoryName) {
        try {
            log.info("Executing cache invalidation keys deletion for queryId={}, categoryName={}", queryId, categoryName);

            // 1. Delete single query key: query:{id}
            if (queryId != null) {
                String singleQueryKey = "query:" + queryId;
                redisTemplate.delete(singleQueryKey);
                log.info("Deleted key={}", singleQueryKey);
            }

            // 2. Delete all queries list keys: queries:all*
            Set<String> allKeys = redisTemplate.keys("queries:all*");
            if (allKeys != null && !allKeys.isEmpty()) {
                redisTemplate.delete(allKeys);
                log.info("Deleted keys matching 'queries:all*': {}", allKeys);
            }

            // 3. Delete latest queries list keys: queries:latest*
            Set<String> latestKeys = redisTemplate.keys("queries:latest*");
            if (latestKeys != null && !latestKeys.isEmpty()) {
                redisTemplate.delete(latestKeys);
                log.info("Deleted keys matching 'queries:latest*': {}", latestKeys);
            }

            // 4. Delete category queries list keys: queries:category:{categoryName}*
            if (categoryName != null && !categoryName.isBlank()) {
                String pattern = "queries:category:" + categoryName + "*";
                Set<String> categoryKeys = redisTemplate.keys(pattern);
                if (categoryKeys != null && !categoryKeys.isEmpty()) {
                    redisTemplate.delete(categoryKeys);
                    log.info("Deleted keys matching '{}': {}", pattern, categoryKeys);
                }
            } else {
                Set<String> categoryKeys = redisTemplate.keys("queries:category:*");
                if (categoryKeys != null && !categoryKeys.isEmpty()) {
                    redisTemplate.delete(categoryKeys);
                    log.info("Deleted all keys matching 'queries:category:*': {}", categoryKeys);
                }
            }

        } catch (Exception e) {
            log.error("Failed to execute cache invalidation for queryId={}, categoryName={}", queryId, categoryName, e);
        }
    }
}
