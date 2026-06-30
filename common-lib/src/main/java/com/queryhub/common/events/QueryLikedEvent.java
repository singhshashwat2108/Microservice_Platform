package com.queryhub.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Kafka event published when a query receives a like.
 * Extends NotificationEvent with no additional fields.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class QueryLikedEvent extends NotificationEvent {
    // All required fields are inherited from NotificationEvent
}
