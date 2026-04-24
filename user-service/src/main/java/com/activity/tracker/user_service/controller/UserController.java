package com.activity.tracker.user_service.controller;

import com.activity.tracker.user_service.dto.CreateUserRequest;
import com.activity.tracker.user_service.dto.UpdateUserRequest;
import com.activity.tracker.user_service.model.AppUser;
import com.activity.tracker.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Flux<AppUser> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppUser>> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<AppUser>> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<AppUser>> create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppUser>> update(@PathVariable Long id,
                                                @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
