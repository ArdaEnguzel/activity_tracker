package com.activity.tracker.order_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private String productName;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;
}