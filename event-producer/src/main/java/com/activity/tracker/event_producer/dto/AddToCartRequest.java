package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToCartRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    @NotBlank
    private String cartId;

    @NotBlank
    private String productId;

    @NotBlank
    private String productName;

    private String category;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;

    @NotBlank
    private String currency;
}