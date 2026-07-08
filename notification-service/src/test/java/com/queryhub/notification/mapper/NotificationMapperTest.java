package com.queryhub.notification.mapper;

import com.queryhub.common.events.CommentAddedEvent;
import com.queryhub.common.events.EventType;
import com.queryhub.common.events.QueryCreatedEvent;
import com.queryhub.common.events.QueryLikedEvent;
import com.queryhub.notification.entity.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for NotificationMapper — verifies correct message generation
 * and entity mapping for each event type.
 */
class NotificationMapperTest {

    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
    }

    @Test
    @DisplayName("fromQueryCreatedEvent() generates correct message and entity")
    void fromQueryCreatedEvent_shouldMapCorrectly() {
        QueryCreatedEvent event = QueryCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .queryId(1L)
                .categoryName("Java")
                .queryTitle("What is JVM?")
                .username("alice")
                .createdAt(LocalDateTime.now())
                .build();

        Notification notification = mapper.fromQueryCreatedEvent(event);

        assertThat(notification.getQueryId()).isEqualTo(1L);
        assertThat(notification.getCategoryName()).isEqualTo("Java");
        assertThat(notification.getEventType()).isEqualTo(EventType.QUERY_CREATED);
        assertThat(notification.getActorUsername()).isEqualTo("alice");
        assertThat(notification.getMessage()).isEqualTo("alice created a new query.");
    }

    @Test
    @DisplayName("fromCommentAddedEvent() generates correct message and entity")
    void fromCommentAddedEvent_shouldMapCorrectly() {
        CommentAddedEvent event = CommentAddedEvent.builder()
                .eventId(UUID.randomUUID())
                .queryId(2L)
                .categoryName("Spring Boot")
                .commentId(100L)
                .username("bob")
                .createdAt(LocalDateTime.now())
                .build();

        Notification notification = mapper.fromCommentAddedEvent(event);

        assertThat(notification.getQueryId()).isEqualTo(2L);
        assertThat(notification.getCategoryName()).isEqualTo("Spring Boot");
        assertThat(notification.getEventType()).isEqualTo(EventType.COMMENT_ADDED);
        assertThat(notification.getActorUsername()).isEqualTo("bob");
        assertThat(notification.getMessage()).isEqualTo("bob commented on this query.");
    }

    @Test
    @DisplayName("fromQueryLikedEvent() generates correct message and entity")
    void fromQueryLikedEvent_shouldMapCorrectly() {
        QueryLikedEvent event = QueryLikedEvent.builder()
                .eventId(UUID.randomUUID())
                .queryId(3L)
                .categoryName("Docker")
                .username("charlie")
                .createdAt(LocalDateTime.now())
                .build();

        Notification notification = mapper.fromQueryLikedEvent(event);

        assertThat(notification.getQueryId()).isEqualTo(3L);
        assertThat(notification.getCategoryName()).isEqualTo("Docker");
        assertThat(notification.getEventType()).isEqualTo(EventType.QUERY_LIKED);
        assertThat(notification.getActorUsername()).isEqualTo("charlie");
        assertThat(notification.getMessage()).isEqualTo("charlie liked this query.");
    }
}
