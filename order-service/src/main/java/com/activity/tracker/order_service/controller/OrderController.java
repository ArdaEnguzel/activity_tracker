package com.activity.tracker.order_service.controller;

import com.activity.tracker.order_service.dto.CreateOrderRequest;
import com.activity.tracker.order_service.dto.UpdateOrderStatusRequest;
import com.activity.tracker.order_service.model.Order;
import com.activity.tracker.order_service.model.OrderItem;
import com.activity.tracker.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/user/{userId}")
    public Flux<Order> getByUser(@PathVariable String userId) {
        return orderService.findByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public Flux<Order> getByStatus(@PathVariable String status) {
        return orderService.findByStatus(status);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{id}/items")
    public Flux<OrderItem> getItems(@PathVariable Long id) {
        return orderService.findItemsByOrderId(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Order>> updateStatus(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateStatus(id, request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return orderService.delete(id);
    }
}