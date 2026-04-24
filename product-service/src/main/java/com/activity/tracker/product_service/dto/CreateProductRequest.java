package com.activity.tracker.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String description;

    private String category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String currency;

    @NotNull
    @PositiveOrZero
    private Integer stockQuantity;

    private String imageUrl;
}