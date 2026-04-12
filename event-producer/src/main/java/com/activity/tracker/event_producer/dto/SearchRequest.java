package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String sessionId;

    @NotBlank
    private String query;

    private Map<String, String> filters;

    private List<String> resultProductIds;

    @NotNull
    @PositiveOrZero
    private Integer resultCount;

    @PositiveOrZero
    private Long responseTimeMs;
}