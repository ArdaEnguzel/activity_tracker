package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddToCartRequest(
        @NotBlank String userId,
        @NotBlank String sessionId,
        @NotBlank String cartId,
        @NotBlank String productId,
        @NotBlank String productName,
        String category,
        @NotNull @Positive Integer quantity,
        @NotNull @Positive BigDecimal unitPrice,
        @NotBlank String currency
) {}
