package com.queryhub.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class for all Kafka notification events.
 * Shared fields are defined here to avoid duplication across event types.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class NotificationEvent {

    /** Unique identifier for this event instance (for deduplication / tracing). */
    private UUID eventId;

    /** ID of the query this event relates to. */
    private Long queryId;

    /** Category name of the query (replaces queryTopic for consistent naming). */
    private String categoryName;

    /** Username of the user who triggered this event. */
    private String username;

    /** Timestamp when the event was created. */
    private LocalDateTime createdAt;
}
