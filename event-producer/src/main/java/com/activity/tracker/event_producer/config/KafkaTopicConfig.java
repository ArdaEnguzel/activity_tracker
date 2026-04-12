package com.activity.tracker.event_producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic productViewTopic() {
        return TopicBuilder.name("PRODUCT_VIEW").partitions(3).build();
    }

    @Bean
    public NewTopic searchTopic() {
        return TopicBuilder.name("SEARCH").partitions(3).build();
    }

    @Bean
    public NewTopic addToCartTopic() {
        return TopicBuilder.name("ADD_TO_CART").partitions(3).build();
    }
}