package com.queryhub.query.kafka;

import com.queryhub.common.events.CommentAddedEvent;
import com.queryhub.common.events.QueryCreatedEvent;
import com.queryhub.common.events.QueryLikedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Publishes Kafka events ONLY after successful database transaction commits.
 * Uses the same TransactionSynchronization pattern as CacheInvalidationService.
 * Publishing failures are logged and never propagated to callers.
 */
@Component
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private static final String TOPIC_QUERY_CREATED = "query-created";
    private static final String TOPIC_COMMENT_ADDED = "comment-added";
    private static final String TOPIC_QUERY_LIKED   = "query-liked";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Schedules publishing of a QueryCreatedEvent after the current transaction commits.
     */
    public void publishQueryCreated(QueryCreatedEvent event) {
        scheduleAfterCommit(() -> doPublish(TOPIC_QUERY_CREATED, event.getQueryId().toString(), event,
                "QueryCreatedEvent", event.getQueryId(), event.getCategoryName()));
    }

    /**
     * Schedules publishing of a CommentAddedEvent after the current transaction commits.
     */
    public void publishCommentAdded(CommentAddedEvent event) {
        scheduleAfterCommit(() -> doPublish(TOPIC_COMMENT_ADDED, event.getQueryId().toString(), event,
                "CommentAddedEvent", event.getQueryId(), event.getCategoryName()));
    }

    /**
     * Schedules publishing of a QueryLikedEvent after the current transaction commits.
     */
    public void publishQueryLiked(QueryLikedEvent event) {
        scheduleAfterCommit(() -> doPublish(TOPIC_QUERY_LIKED, event.getQueryId().toString(), event,
                "QueryLikedEvent", event.getQueryId(), event.getCategoryName()));
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Registers a Runnable to execute after the current transaction commits.
     * If no active transaction is present, executes immediately.
     */
    private void scheduleAfterCommit(Runnable action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            // No transaction active — publish immediately (e.g., in tests)
            action.run();
        }
    }

    /**
     * Performs the actual Kafka send and logs the result.
     */
    private void doPublish(String topic, String key, Object event,
                           String eventName, Long queryId, String categoryName) {
        try {
            kafkaTemplate.send(topic, key, event);
            log.info("Published {} | eventId={} | queryId={} | category={}",
                    eventName,
                    extractEventId(event),
                    queryId,
                    categoryName);
        } catch (Exception e) {
            log.error("Failed to publish {} for queryId={}: {}", eventName, queryId, e.getMessage(), e);
            // Do NOT rethrow — publishing failure must not break the main flow
        }
    }

    private String extractEventId(Object event) {
        if (event instanceof com.queryhub.common.events.NotificationEvent ne) {
            return ne.getEventId() != null ? ne.getEventId().toString() : "N/A";
        }
        return "N/A";
    }
}
