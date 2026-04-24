package com.activity.tracker.product_service.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    private String name;
    private String description;
    private String category;

    @Positive
    private BigDecimal price;

    private String currency;

    @PositiveOrZero
    private Integer stockQuantity;

    private String imageUrl;
    private Boolean active;
}