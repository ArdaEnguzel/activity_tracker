package com.activity.tracker.event_producer.controller;

import com.activity.tracker.event_producer.dto.AddToCartRequest;
import com.activity.tracker.event_producer.dto.ProductViewRequest;
import com.activity.tracker.event_producer.dto.SearchRequest;
import com.activity.tracker.event_producer.service.AddToCartService;
import com.activity.tracker.event_producer.service.ProductViewService;
import com.activity.tracker.event_producer.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class ActivityEventController {

    private static final Retry KAFKA_RETRY = Retry
            .backoff(3, Duration.ofMillis(500))
            .maxBackoff(Duration.ofSeconds(5))
            .filter(ex -> ex instanceof KafkaException)
            .onRetryExhaustedThrow((spec, signal) -> signal.failure());

    private final ProductViewService productViewService;
    private final SearchService searchService;
    private final AddToCartService addToCartService;

    @PostMapping("/product-view")
    public Mono<ResponseEntity<String>> trackProductView(@Valid @RequestBody ProductViewRequest request) {
        return Mono.fromFuture(productViewService.send(request))
                .retryWhen(KAFKA_RETRY)
                .map(x -> ResponseEntity.accepted().body("PRODUCT_VIEW event accepted"))
                .onErrorResume(ex -> Mono.just(
                        ResponseEntity.internalServerError().body("Failed to publish PRODUCT_VIEW: " + ex.getMessage())
                ));
    }

    @PostMapping("/search")
    public Mono<ResponseEntity<String>> trackSearch(@Valid @RequestBody SearchRequest request) {
        return Mono.fromFuture(searchService.send(request))
                .retryWhen(KAFKA_RETRY)
                .map(x -> ResponseEntity.accepted().body("SEARCH event accepted"))
                .onErrorResume(ex -> Mono.just(
                        ResponseEntity.internalServerError().body("Failed to publish SEARCH: " + ex.getMessage())
                ));
    }

    @PostMapping("/add-to-cart")
    public Mono<ResponseEntity<String>> trackAddToCart(@Valid @RequestBody AddToCartRequest request) {
        return Mono.fromFuture(addToCartService.send(request))
                .retryWhen(KAFKA_RETRY)
                .map(x -> ResponseEntity.accepted().body("ADD_TO_CART event accepted"))
                .onErrorResume(ex -> Mono.just(
                        ResponseEntity.internalServerError().body("Failed to publish ADD_TO_CART: " + ex.getMessage())
                ));
    }
}