package com.queryhub.notification.controller;

import com.queryhub.notification.dto.NotificationDto;
import com.queryhub.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller exposing notification endpoints.
 * Accessed via API Gateway at /api/notifications/**
 *
 * GET    /notifications         - all notifications, newest first
 * DELETE /notifications/{id}    - delete single notification
 * DELETE /notifications         - delete all notifications
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Returns all notifications ordered by creation timestamp (newest first).
     */
    @GetMapping
    public ResponseEntity<List<NotificationDto.NotificationResponse>> getAllNotifications() {
        List<NotificationDto.NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Deletes a single notification by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Notification " + id + " deleted successfully."));
    }

    /**
     * Deletes all notifications.
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAllNotifications() {
        notificationService.deleteAll();
        return ResponseEntity.ok(Map.of("message", "All notifications deleted successfully."));
    }
}
