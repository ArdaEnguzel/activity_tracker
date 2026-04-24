package com.activity.tracker.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String currency;

    private String shippingAddress;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}