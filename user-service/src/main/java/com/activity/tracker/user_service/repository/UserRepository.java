package com.activity.tracker.user_service.repository;

import com.activity.tracker.user_service.model.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<AppUser, Long> {

    Mono<AppUser> findByUsername(String username);

    Mono<AppUser> findByEmail(String email);

    Flux<AppUser> findByActiveTrue();

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);
}