package com.queryhub.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Notification Service - Phase 4
 * Consumes Kafka events from query-service and persists activity notifications
 * into its own notification_db database.
 *
 * Port: 8084
 * Topics consumed: query-created, comment-added, query-liked
 */
@SpringBootApplication(scanBasePackages = {"com.queryhub.notification", "com.queryhub.common"})
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
