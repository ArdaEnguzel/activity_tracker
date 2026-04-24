package com.activity.tracker.order_service.repository;

import com.activity.tracker.order_service.model.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {

    Flux<OrderItem> findByOrderId(Long orderId);

    Mono<Void> deleteByOrderId(Long orderId);
}