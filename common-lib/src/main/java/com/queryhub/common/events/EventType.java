package com.queryhub.common.events;

/**
 * Strongly-typed event type enum shared across all services.
 * Used in both query-service (publishing) and notification-service (consuming).
 */
public enum EventType {
    QUERY_CREATED,
    COMMENT_ADDED,
    QUERY_LIKED
}
