package com.activity.tracker.event_producer.service;

import com.activity.tracker.event_producer.dto.SearchRequest;
import com.activity.tracker.event_producer.model.SearchEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private static final String TOPIC = "SEARCH";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<String, String>> send(SearchRequest request) {
        SearchEvent event = SearchEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .userId(request.userId())
                .sessionId(request.sessionId())
                .query(request.query())
                .filters(request.filters())
                .resultProductIds(request.resultProductIds())
                .resultCount(request.resultCount())
                .responseTimeMs(request.responseTimeMs())
                .build();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize SearchEvent: userId={}", event.getUserId(), e);
            return CompletableFuture.failedFuture(e);
        }

        log.info("Sending SEARCH event | eventId={} userId={} query=\"{}\" resultCount={}",
                event.getEventId(), event.getUserId(), event.getQuery(), event.getResultCount());

        return kafkaTemplate.send(TOPIC, event.getUserId(), payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("SEARCH send failed | eventId={}", event.getEventId(), ex);
                    } else {
                        log.info("SEARCH sent | eventId={} partition={} offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}