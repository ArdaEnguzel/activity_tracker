package com.activity.tracker.order_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<ProductResponse> getProduct(Long productId) {
        return webClientBuilder.build()
                .get()
                .uri("http://product-service/api/products/{id}", productId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new IllegalArgumentException("Product not found: " + productId)))
                .bodyToMono(ProductResponse.class);
    }
}