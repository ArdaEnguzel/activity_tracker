package com.activity.tracker.product_service.controller;

import com.activity.tracker.product_service.dto.CreateProductRequest;
import com.activity.tracker.product_service.dto.UpdateProductRequest;
import com.activity.tracker.product_service.model.Product;
import com.activity.tracker.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<Product> getAll(@RequestParam(required = false) String category) {
        if (category != null) {
            return productService.findByCategory(category);
        }
        return productService.findActive();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@Valid @RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update(@PathVariable Long id,
                                                @Valid @RequestBody UpdateProductRequest request) {
        return productService.update(id, request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}