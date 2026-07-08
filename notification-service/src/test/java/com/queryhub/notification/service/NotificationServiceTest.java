package com.queryhub.notification.service;

import com.queryhub.common.events.EventType;
import com.queryhub.notification.dto.NotificationDto;
import com.queryhub.notification.entity.Notification;
import com.queryhub.notification.mapper.NotificationMapper;
import com.queryhub.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationService.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    private Notification testNotification;
    private NotificationDto.NotificationResponse testResponse;

    @BeforeEach
    void setUp() {
        testNotification = Notification.builder()
                .id(1L)
                .queryId(10L)
                .categoryName("Java")
                .eventType(EventType.QUERY_CREATED)
                .actorUsername("shashwat")
                .message("shashwat created a new query.")
                .createdAt(LocalDateTime.now())
                .build();

        testResponse = NotificationDto.NotificationResponse.builder()
                .id(1L)
                .queryId(10L)
                .categoryName("Java")
                .eventType(EventType.QUERY_CREATED)
                .actorUsername("shashwat")
                .message("shashwat created a new query.")
                .createdAt(testNotification.getCreatedAt())
                .build();
    }

    @Test
    @DisplayName("save() should persist and return the notification")
    void save_shouldPersistNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification result = notificationService.save(testNotification);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEventType()).isEqualTo(EventType.QUERY_CREATED);
        verify(notificationRepository, times(1)).save(testNotification);
    }

    @Test
    @DisplayName("getAllNotifications() should return notifications newest first")
    void getAllNotifications_shouldReturnInOrder() {
        when(notificationRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(testNotification));
        when(notificationMapper.toResponse(testNotification)).thenReturn(testResponse);

        List<NotificationDto.NotificationResponse> result = notificationService.getAllNotifications();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActorUsername()).isEqualTo("shashwat");
        verify(notificationRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("deleteById() should delete existing notification")
    void deleteById_shouldDeleteExistingNotification() {
        when(notificationRepository.existsById(1L)).thenReturn(true);

        notificationService.deleteById(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById() should throw ResourceNotFoundException for missing id")
    void deleteById_shouldThrowForMissingId() {
        when(notificationRepository.existsById(99L)).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(
                com.queryhub.common.handler.ResourceNotFoundException.class,
                () -> notificationService.deleteById(99L));
    }

    @Test
    @DisplayName("deleteAll() should remove all notifications")
    void deleteAll_shouldRemoveAll() {
        when(notificationRepository.count()).thenReturn(3L);

        notificationService.deleteAll();

        verify(notificationRepository).deleteAll();
    }
}
