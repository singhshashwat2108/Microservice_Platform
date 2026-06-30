package com.queryhub.notification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.queryhub.common.events.CommentAddedEvent;
import com.queryhub.common.events.QueryCreatedEvent;
import com.queryhub.common.events.QueryLikedEvent;
import com.queryhub.notification.entity.Notification;
import com.queryhub.notification.mapper.NotificationMapper;
import com.queryhub.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka consumer for notification-service.
 * Listens on three topics: query-created, comment-added, query-liked.
 *
 * Architecture:
 *   Kafka Event → NotificationConsumer → NotificationMapper → Notification Entity → NotificationService → Repository
 *
 * Consumer never constructs entities directly. All mapping is delegated to NotificationMapper.
 * Malformed messages are caught, logged, and skipped — the consumer never crashes.
 */
@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(NotificationService notificationService,
                                NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        // Configure ObjectMapper with JavaTimeModule for LocalDateTime deserialization
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(
                com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // -------------------------------------------------------------------------
    // Kafka Listeners
    // -------------------------------------------------------------------------

    @KafkaListener(topics = "query-created", groupId = "notification-service-group")
    public void handleQueryCreated(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Object payload = record.value();
            QueryCreatedEvent event = convertPayload(payload, QueryCreatedEvent.class);
            log.info("Received QueryCreatedEvent | eventId={} | queryId={} | category={} | username={}",
                    event.getEventId(), event.getQueryId(), event.getCategoryName(), event.getUsername());

            Notification notification = notificationMapper.fromQueryCreatedEvent(event);
            Notification saved = notificationService.save(notification);
            log.info("Persisted Notification id={} for QueryCreatedEvent eventId={}",
                    saved.getId(), event.getEventId());

        } catch (Exception e) {
            log.error("Failed to process message from topic='{}': {} — skipping.", topic, e.getMessage(), e);
            // Do NOT rethrow — consumer must continue on bad messages
        }
    }

    @KafkaListener(topics = "comment-added", groupId = "notification-service-group")
    public void handleCommentAdded(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Object payload = record.value();
            CommentAddedEvent event = convertPayload(payload, CommentAddedEvent.class);
            log.info("Received CommentAddedEvent | eventId={} | queryId={} | commentId={} | username={}",
                    event.getEventId(), event.getQueryId(), event.getCommentId(), event.getUsername());

            Notification notification = notificationMapper.fromCommentAddedEvent(event);
            Notification saved = notificationService.save(notification);
            log.info("Persisted Notification id={} for CommentAddedEvent eventId={}",
                    saved.getId(), event.getEventId());

        } catch (Exception e) {
            log.error("Failed to process message from topic='{}': {} — skipping.", topic, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "query-liked", groupId = "notification-service-group")
    public void handleQueryLiked(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> record,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Object payload = record.value();
            QueryLikedEvent event = convertPayload(payload, QueryLikedEvent.class);
            log.info("Received QueryLikedEvent | eventId={} | queryId={} | username={}",
                    event.getEventId(), event.getQueryId(), event.getUsername());

            Notification notification = notificationMapper.fromQueryLikedEvent(event);
            Notification saved = notificationService.save(notification);
            log.info("Persisted Notification id={} for QueryLikedEvent eventId={}",
                    saved.getId(), event.getEventId());

        } catch (Exception e) {
            log.error("Failed to process message from topic='{}': {} — skipping.", topic, e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Helper: safely convert payload Map → typed event
    // -------------------------------------------------------------------------

    /**
     * Converts the deserialized payload (may arrive as Map or already typed) into the
     * target event class. Handles both raw Map (when type headers are absent) and
     * already-typed objects.
     */
    @SuppressWarnings("unchecked")
    private <T> T convertPayload(Object payload, Class<T> targetType) {
        if (targetType.isInstance(payload)) {
            return targetType.cast(payload);
        }
        // Payload arrived as a Map (type header missing or fallback)
        return objectMapper.convertValue(payload, targetType);
    }
}
