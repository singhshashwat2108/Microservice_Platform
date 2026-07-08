package com.queryhub.common.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Kafka event published when a comment is added to a query.
 * Extends NotificationEvent with the comment ID.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CommentAddedEvent extends NotificationEvent {

    /** ID of the newly created comment. */
    private Long commentId;
}
