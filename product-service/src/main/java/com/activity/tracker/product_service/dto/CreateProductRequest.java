package com.activity.tracker.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String name,
        String description,
        String category,
        @NotNull @Positive BigDecimal price,
        @NotBlank String currency,
        @NotNull @PositiveOrZero Integer stockQuantity,
        String imageUrl
) {}
