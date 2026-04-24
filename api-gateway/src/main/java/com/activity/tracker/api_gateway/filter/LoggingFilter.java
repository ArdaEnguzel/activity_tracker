package com.activity.tracker.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long start = Instant.now().toEpochMilli();

        log.info("→ {} {} [{}]",
                request.getMethod(),
                request.getURI().getPath(),
                request.getRemoteAddress());

        return chain.filter(exchange).doFinally(signal -> {
            long elapsed = Instant.now().toEpochMilli() - start;
            log.info("← {} {} {}ms [status={}]",
                    request.getMethod(),
                    request.getURI().getPath(),
                    elapsed,
                    exchange.getResponse().getStatusCode());
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
