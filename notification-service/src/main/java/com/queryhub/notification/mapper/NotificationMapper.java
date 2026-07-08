package com.queryhub.notification.mapper;

import com.queryhub.common.events.CommentAddedEvent;
import com.queryhub.common.events.EventType;
import com.queryhub.common.events.QueryCreatedEvent;
import com.queryhub.common.events.QueryLikedEvent;
import com.queryhub.notification.entity.Notification;
import org.springframework.stereotype.Component;

/**
 * Converts Kafka event objects into Notification entities.
 *
 * NotificationConsumer → NotificationMapper → Notification Entity → NotificationService → Repository
 *
 * This class is the ONLY place where notification messages are generated.
 * Query Service must never generate messages.
 */
@Component
public class NotificationMapper {

    /**
     * Maps a QueryCreatedEvent into a Notification entity.
     * Message: "{username} created a new query."
     */
    public Notification fromQueryCreatedEvent(QueryCreatedEvent event) {
        String message = event.getUsername() + " created a new query.";
        return Notification.builder()
                .queryId(event.getQueryId())
                .categoryName(event.getCategoryName())
                .eventType(EventType.QUERY_CREATED)
                .actorUsername(event.getUsername())
                .message(message)
                .build();
    }

    /**
     * Maps a CommentAddedEvent into a Notification entity.
     * Message: "{username} commented on this query."
     */
    public Notification fromCommentAddedEvent(CommentAddedEvent event) {
        String message = event.getUsername() + " commented on this query.";
        return Notification.builder()
                .queryId(event.getQueryId())
                .categoryName(event.getCategoryName())
                .eventType(EventType.COMMENT_ADDED)
                .actorUsername(event.getUsername())
                .message(message)
                .build();
    }

    /**
     * Maps a QueryLikedEvent into a Notification entity.
     * Message: "{username} liked this query."
     */
    public Notification fromQueryLikedEvent(QueryLikedEvent event) {
        String message = event.getUsername() + " liked this query.";
        return Notification.builder()
                .queryId(event.getQueryId())
                .categoryName(event.getCategoryName())
                .eventType(EventType.QUERY_LIKED)
                .actorUsername(event.getUsername())
                .message(message)
                .build();
    }

    /**
     * Converts a Notification entity to a DTO response.
     */
    public com.queryhub.notification.dto.NotificationDto.NotificationResponse toResponse(Notification notification) {
        return com.queryhub.notification.dto.NotificationDto.NotificationResponse.builder()
                .id(notification.getId())
                .queryId(notification.getQueryId())
                .categoryName(notification.getCategoryName())
                .eventType(notification.getEventType())
                .actorUsername(notification.getActorUsername())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
