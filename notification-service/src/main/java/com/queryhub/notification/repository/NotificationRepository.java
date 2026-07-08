package com.queryhub.notification.repository;

import com.queryhub.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Notification entity.
 * Returns notifications ordered by createdAt DESC (newest first) per requirement.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Returns all notifications ordered by creation timestamp descending.
     * Used by GET /notifications endpoint.
     */
    List<Notification> findAllByOrderByCreatedAtDesc();
}
