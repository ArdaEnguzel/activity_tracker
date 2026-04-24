package com.activity.tracker.user_service.service;

import com.activity.tracker.user_service.dto.CreateUserRequest;
import com.activity.tracker.user_service.dto.UpdateUserRequest;
import com.activity.tracker.user_service.model.AppUser;
import com.activity.tracker.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<AppUser> findAll() {
        return userRepository.findByActiveTrue();
    }

    public Mono<AppUser> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found: " + id)));
    }

    public Mono<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found: " + username)));
    }

    public Mono<AppUser> create(CreateUserRequest request) {
        return userRepository.existsByUsername(request.getUsername())
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new IllegalArgumentException(
                                "Username already taken: " + request.getUsername()));
                    }
                    return userRepository.existsByEmail(request.getEmail());
                })
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new IllegalArgumentException(
                                "Email already registered: " + request.getEmail()));
                    }
                    LocalDateTime now = LocalDateTime.now();
                    AppUser user = AppUser.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .phoneNumber(request.getPhoneNumber())
                            .active(true)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                    log.info("Creating user: username={} email={}", user.getUsername(), user.getEmail());
                    return userRepository.save(user);
                });
    }

    public Mono<AppUser> update(Long id, UpdateUserRequest request) {
        return findById(id).flatMap(existing -> {
            if (request.getEmail() != null)       existing.setEmail(request.getEmail());
            if (request.getFirstName() != null)   existing.setFirstName(request.getFirstName());
            if (request.getLastName() != null)    existing.setLastName(request.getLastName());
            if (request.getPhoneNumber() != null) existing.setPhoneNumber(request.getPhoneNumber());
            if (request.getActive() != null)      existing.setActive(request.getActive());
            existing.setUpdatedAt(LocalDateTime.now());

            log.info("Updating user: id={}", id);
            return userRepository.save(existing);
        });
    }

    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(user -> {
            log.info("Deleting user: id={} username={}", id, user.getUsername());
            return userRepository.deleteById(id);
        });
    }
}