package com.activity.tracker.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank String userId,
        @NotBlank String currency,
        String shippingAddress,
        @NotEmpty @Valid List<OrderItemRequest> items
) {}
