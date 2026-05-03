package com.activity.tracker.order_service.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String currency;
    private Integer stockQuantity;
    private Boolean active;
}