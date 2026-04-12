package com.activity.tracker.event_producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEvent {

    private String eventId;
    private String userId;
    private String sessionId;
    private String query;
    private Map<String, String> filters;   // e.g. {"category": "electronics", "brand": "samsung"}
    private List<String> resultProductIds; // top returned product IDs
    private Integer resultCount;
    private Long responseTimeMs;
    private LocalDateTime timestamp;
}