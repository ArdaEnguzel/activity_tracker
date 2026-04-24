package com.activity.tracker.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/{service}")
    public Mono<ResponseEntity<Map<String, String>>> fallback(@PathVariable String service) {
        Map<String, String> body = Map.of(
                "status", "SERVICE_UNAVAILABLE",
                "service", service,
                "message", service + " is currently unavailable. Please try again later."
        );
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body));
    }
}
