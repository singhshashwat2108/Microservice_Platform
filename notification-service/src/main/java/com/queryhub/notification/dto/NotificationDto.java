package com.queryhub.notification.dto;

import com.queryhub.common.events.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Objects for the Notification REST API.
 */
public class NotificationDto {

    /**
     * Response DTO returned by GET /notifications.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponse {

        private Long id;
        private Long queryId;
        private String categoryName;
        private EventType eventType;
        private String actorUsername;
        private String message;
        private LocalDateTime createdAt;
    }
}
