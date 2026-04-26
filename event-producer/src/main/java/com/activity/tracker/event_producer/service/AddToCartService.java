package com.activity.tracker.event_producer.service;

import com.activity.tracker.event_producer.dto.AddToCartRequest;
import com.activity.tracker.event_producer.model.AddToCartEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddToCartService {

    private static final String TOPIC = "ADD_TO_CART";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<SendResult<String, String>> send(AddToCartRequest request) {
        BigDecimal totalPrice = request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));

        AddToCartEvent event = AddToCartEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .userId(request.userId())
                .sessionId(request.sessionId())
                .cartId(request.cartId())
                .productId(request.productId())
                .productName(request.productName())
                .category(request.category())
                .quantity(request.quantity())
                .unitPrice(request.unitPrice())
                .totalPrice(totalPrice)
                .currency(request.currency())
                .build();

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AddToCartEvent: userId={}", event.getUserId(), e);
            return CompletableFuture.failedFuture(e);
        }

        log.info("Sending ADD_TO_CART event | eventId={} userId={} productId={} quantity={} totalPrice={}",
                event.getEventId(), event.getUserId(), event.getProductId(),
                event.getQuantity(), event.getTotalPrice());

        return kafkaTemplate.send(TOPIC, event.getUserId(), payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("ADD_TO_CART send failed | eventId={}", event.getEventId(), ex);
                    } else {
                        log.info("ADD_TO_CART sent | eventId={} partition={} offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
