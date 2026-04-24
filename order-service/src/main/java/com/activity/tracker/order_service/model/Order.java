package com.activity.tracker.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {

    @Id
    private Long id;

    private String userId;

    private String status;

    private BigDecimal totalAmount;
    private String currency;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}