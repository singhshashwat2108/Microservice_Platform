package com.queryhub.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Kafka event published when a new query is created.
 * Extends NotificationEvent with the query title.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class QueryCreatedEvent extends NotificationEvent {

    /** Title of the newly created query. */
    private String queryTitle;
}
