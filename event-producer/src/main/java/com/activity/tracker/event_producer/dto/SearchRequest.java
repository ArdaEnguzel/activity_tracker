package com.activity.tracker.event_producer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.Map;

public record SearchRequest(
        @NotBlank String userId,
        @NotBlank String sessionId,
        @NotBlank String query,
        Map<String, String> filters,
        List<String> resultProductIds,
        @NotNull @PositiveOrZero Integer resultCount,
        @PositiveOrZero Long responseTimeMs
) {}
