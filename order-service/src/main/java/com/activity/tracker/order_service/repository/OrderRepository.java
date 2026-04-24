package com.activity.tracker.order_service.repository;

import com.activity.tracker.order_service.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByUserId(String userId);

    Flux<Order> findByStatus(String status);

    Flux<Order> findByUserIdAndStatus(String userId, String status);
}