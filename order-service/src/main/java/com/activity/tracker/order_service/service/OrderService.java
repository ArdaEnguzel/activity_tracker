package com.activity.tracker.order_service.service;

import com.activity.tracker.order_service.client.ProductServiceClient;
import com.activity.tracker.order_service.client.UserServiceClient;
import com.activity.tracker.order_service.dto.CreateOrderRequest;
import com.activity.tracker.order_service.dto.OrderItemRequest;
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
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

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
        long userId;
        try {
            userId = Long.parseLong(request.userId());
        } catch (NumberFormatException e) {
            return Mono.error(new IllegalArgumentException("Invalid userId: " + request.userId()));
        }

        return userServiceClient.getUser(userId)
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "User not found or inactive: " + request.userId())))
                .flatMap(user -> resolveItems(request.items()))
                .flatMap(resolvedItems -> {
                    BigDecimal totalAmount = resolvedItems.stream()
                            .map(ResolvedItem::totalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    LocalDateTime now = LocalDateTime.now();
                    Order order = Order.builder()
                            .userId(request.userId())
                            .status("PENDING")
                            .totalAmount(totalAmount)
                            .currency(request.currency())
                            .shippingAddress(request.shippingAddress())
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                List<OrderItem> items = resolvedItems.stream()
                                        .map(r -> OrderItem.builder()
                                                .orderId(savedOrder.getId())
                                                .productId(r.productId())
                                                .productName(r.productName())
                                                .quantity(r.quantity())
                                                .unitPrice(r.unitPrice())
                                                .totalPrice(r.totalPrice())
                                                .build())
                                        .toList();

                                log.info("Creating order: orderId={} userId={} itemCount={} total={}",
                                        savedOrder.getId(), savedOrder.getUserId(),
                                        items.size(), savedOrder.getTotalAmount());

                                return orderItemRepository.saveAll(items).then(Mono.just(savedOrder));
                            });
                });
    }

    private Mono<List<ResolvedItem>> resolveItems(List<OrderItemRequest> itemRequests) {
        return Flux.fromIterable(itemRequests)
                .flatMap(item -> productServiceClient.getProduct(item.productId())
                        .map(product -> {
                            if (!Boolean.TRUE.equals(product.getActive())) {
                                throw new IllegalArgumentException("Product is not active: " + product.getId());
                            }
                            if (product.getStockQuantity() < item.quantity()) {
                                throw new IllegalArgumentException(
                                        "Insufficient stock for product " + product.getId() +
                                        ": requested=" + item.quantity() +
                                        " available=" + product.getStockQuantity());
                            }
                            BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
                            return new ResolvedItem(product.getId(), product.getName(),
                                    item.quantity(), product.getPrice(), total);
                        }))
                .collectList();
    }

    public Mono<Order> updateStatus(Long id, UpdateOrderStatusRequest request) {
        String newStatus = request.status().toUpperCase();
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

    private record ResolvedItem(Long productId, String productName, Integer quantity,
                                BigDecimal unitPrice, BigDecimal totalPrice) {}
}