package com.activity.tracker.event_producer.service;

import com.activity.tracker.event_producer.dto.ProductViewRequest;
import com.activity.tracker.event_producer.model.ProductViewEvent;
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
public class ProductViewService {

    private static final String TOPIC = "PRODUCT_VIEW";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final JsonService jsonService;

    public CompletableFuture<SendResult<String, String>> send(ProductViewRequest request) {
        ProductViewEvent event = ProductViewEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .userId(request.userId())
                .sessionId(request.sessionId())
                .productId(request.productId())
                .productName(request.productName())
                .category(request.category())
                .price(request.price())
                .currency(request.currency())
                .referrer(request.referrer())
                .build();


        String payload = jsonService.toJson(event);

        log.info("Sending PRODUCT_VIEW event | eventId={} userId={} productId={}",
                event.getEventId(), event.getUserId(), event.getProductId());

        return kafkaTemplate.send(TOPIC, event.getUserId(), payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("PRODUCT_VIEW send failed | eventId={}", event.getEventId(), ex);
                    } else {
                        log.info("PRODUCT_VIEW sent | eventId={} partition={} offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}