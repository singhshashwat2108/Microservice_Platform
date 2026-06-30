package com.queryhub.query.service;

import com.queryhub.common.events.QueryLikedEvent;
import com.queryhub.common.handler.ResourceNotFoundException;
import com.queryhub.query.entity.LikeEntity;
import com.queryhub.query.entity.QueryEntity;
import com.queryhub.query.kafka.KafkaEventPublisher;
import com.queryhub.query.repository.LikeRepository;
import com.queryhub.query.repository.QueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final QueryRepository queryRepository;
    private final CacheInvalidationService cacheInvalidationService;
    private final KafkaEventPublisher kafkaEventPublisher;

    public LikeService(LikeRepository likeRepository, QueryRepository queryRepository,
                       CacheInvalidationService cacheInvalidationService,
                       KafkaEventPublisher kafkaEventPublisher) {
        this.likeRepository = likeRepository;
        this.queryRepository = queryRepository;
        this.cacheInvalidationService = cacheInvalidationService;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Transactional
    public void addLike(Long queryId, Long userId, String username) {
        QueryEntity query = queryRepository.findById(queryId)
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + queryId + " not found."));
        if (likeRepository.findByQueryIdAndUserId(queryId, userId).isPresent()) {
            throw new IllegalArgumentException("You have already liked this query.");
        }
        LikeEntity like = LikeEntity.builder()
                .queryId(queryId)
                .userId(userId)
                .build();
        likeRepository.save(like);
        cacheInvalidationService.invalidate(queryId, query.getCategory().getName());

        // Publish Kafka event after DB commit
        QueryLikedEvent event = QueryLikedEvent.builder()
                .eventId(UUID.randomUUID())
                .queryId(queryId)
                .categoryName(query.getCategory().getName())
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();
        kafkaEventPublisher.publishQueryLiked(event);
    }

    @Transactional
    public void removeLike(Long queryId, Long userId) {
        QueryEntity query = queryRepository.findById(queryId)
                .orElseThrow(() -> new ResourceNotFoundException("Query with id " + queryId + " not found."));
        LikeEntity like = likeRepository.findByQueryIdAndUserId(queryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found for this query."));
        likeRepository.delete(like);
        cacheInvalidationService.invalidate(queryId, query.getCategory().getName());
    }

    public long getLikeCount(Long queryId) {
        if (!queryRepository.existsById(queryId)) {
            throw new ResourceNotFoundException("Query with id " + queryId + " not found.");
        }
        return likeRepository.countByQueryId(queryId);
    }
}
