package com.activity.tracker.order_service.service;

import com.activity.tracker.order_service.dto.CreateOrderRequest;
import com.activity.tracker.order_service.dto.UpdateOrderStatusRequest;
import com.activity.tracker.order_service.model.Order;
import com.activity.tracker.order_service.model.OrderItem;
import com.activity.tracker.order_service.repository.OrderItemRepository;
import com.activity.tracker.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final List<String> VALID_STATUSES =
            List.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Flux<Order> findByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Flux<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status.toUpperCase());
    }

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found: " + id)));
    }

    public Flux<OrderItem> findItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public Mono<Order> create(CreateOrderRequest request) {
        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime now = LocalDateTime.now();
        Order order = Order.builder()
                .userId(request.getUserId())
                .status("PENDING")
                .totalAmount(totalAmount)
                .currency(request.getCurrency())
                .shippingAddress(request.getShippingAddress())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return orderRepository.save(order)
                .flatMap(savedOrder -> {
                    List<OrderItem> items = request.getItems().stream()
                            .map(itemReq -> OrderItem.builder()
                                    .orderId(savedOrder.getId())
                                    .productId(itemReq.getProductId())
                                    .productName(itemReq.getProductName())
                                    .quantity(itemReq.getQuantity())
                                    .unitPrice(itemReq.getUnitPrice())
                                    .totalPrice(itemReq.getUnitPrice()
                                            .multiply(BigDecimal.valueOf(itemReq.getQuantity())))
                                    .build())
                            .toList();

                    log.info("Creating order: orderId={} userId={} itemCount={} total={}",
                            savedOrder.getId(), savedOrder.getUserId(),
                            items.size(), savedOrder.getTotalAmount());

                    return orderItemRepository.saveAll(items).then(Mono.just(savedOrder));
                });
    }

    public Mono<Order> updateStatus(Long id, UpdateOrderStatusRequest request) {
        String newStatus = request.getStatus().toUpperCase();
        if (!VALID_STATUSES.contains(newStatus)) {
            return Mono.error(new IllegalArgumentException("Invalid status: " + newStatus));
        }

        return findById(id).flatMap(order -> {
            log.info("Updating order status: orderId={} {} → {}", id, order.getStatus(), newStatus);
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            return orderRepository.save(order);
        });
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(order -> {
            log.info("Deleting order: orderId={}", id);
            return orderItemRepository.deleteByOrderId(id)
                    .then(orderRepository.deleteById(id));
        });
    }
}