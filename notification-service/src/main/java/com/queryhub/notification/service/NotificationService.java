package com.queryhub.notification.service;

import com.queryhub.notification.dto.NotificationDto;
import com.queryhub.notification.entity.Notification;
import com.queryhub.notification.mapper.NotificationMapper;
import com.queryhub.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for the Notification domain.
 * Handles persistence and retrieval of notification records.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Persists a new notification entity.
     * Called by NotificationConsumer via NotificationMapper.
     *
     * @param notification the entity to save
     * @return the saved entity with generated ID
     */
    @Transactional
    public Notification save(Notification notification) {
        Notification saved = notificationRepository.save(notification);
        log.info("Persisted Notification id={} | eventType={} | queryId={} | actor={}",
                saved.getId(), saved.getEventType(), saved.getQueryId(), saved.getActorUsername());
        return saved;
    }

    /**
     * Returns all notifications ordered by createdAt DESC (newest first).
     *
     * @return list of notification response DTOs
     */
    @Transactional(readOnly = true)
    public List<NotificationDto.NotificationResponse> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a single notification by ID.
     *
     * @param id the notification ID
     */
    @Transactional
    public void deleteById(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new com.queryhub.common.handler.ResourceNotFoundException(
                    "Notification with id " + id + " not found.");
        }
        notificationRepository.deleteById(id);
        log.info("Deleted Notification id={}", id);
    }

    /**
     * Deletes all notifications.
     */
    @Transactional
    public void deleteAll() {
        long count = notificationRepository.count();
        notificationRepository.deleteAll();
        log.info("Deleted all {} notifications", count);
    }
}
