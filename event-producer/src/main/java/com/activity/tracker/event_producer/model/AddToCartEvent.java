package com.activity.tracker.event_producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartEvent {

    private String eventId;
    private String userId;
    private String sessionId;
    private String cartId;
    private String productId;
    private String productName;
    private String category;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;     // quantity * unitPrice
    private String currency;
    private LocalDateTime timestamp;
}