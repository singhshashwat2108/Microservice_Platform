package com.queryhub.query.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Declares Kafka topics for query-service events.
 * Topics are auto-created on application startup if they do not exist.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic queryCreatedTopic() {
        return TopicBuilder.name("query-created")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic commentAddedTopic() {
        return TopicBuilder.name("comment-added")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic queryLikedTopic() {
        return TopicBuilder.name("query-liked")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
