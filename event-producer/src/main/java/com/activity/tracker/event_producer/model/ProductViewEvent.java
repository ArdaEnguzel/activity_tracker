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
public class ProductViewEvent {

    private String eventId;
    private String userId;
    private String sessionId;
    private String productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private String currency;
    private String referrer;       // e.g. "HOME", "SEARCH", "RECOMMENDATION"
    private LocalDateTime timestamp;
}