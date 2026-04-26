package com.activity.tracker.product_service.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        String category,
        @Positive BigDecimal price,
        String currency,
        @PositiveOrZero Integer stockQuantity,
        String imageUrl,
        Boolean active
) {}
