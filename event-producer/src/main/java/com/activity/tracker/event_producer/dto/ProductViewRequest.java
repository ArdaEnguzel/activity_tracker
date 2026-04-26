package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductViewRequest(
        @NotBlank String userId,
        @NotBlank String sessionId,
        @NotBlank String productId,
        @NotBlank String productName,
        String category,
        @NotNull @Positive BigDecimal price,
        @NotBlank String currency,
        String referrer
) {}
