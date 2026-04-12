package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductViewRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    @NotBlank
    private String productId;

    @NotBlank
    private String productName;

    private String category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String currency;

    private String referrer;
}