package com.activity.tracker.product_service.service;

import com.activity.tracker.product_service.dto.CreateProductRequest;
import com.activity.tracker.product_service.dto.UpdateProductRequest;
import com.activity.tracker.product_service.model.Product;
import com.activity.tracker.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Flux<Product> findActive() {
        return productRepository.findByActiveTrue();
    }

    public Flux<Product> findByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found: " + id)));
    }

    public Mono<Product> create(CreateProductRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .price(request.price())
                .currency(request.currency())
                .stockQuantity(request.stockQuantity())
                .imageUrl(request.imageUrl())
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        log.info("Creating product: name={} category={}", product.getName(), product.getCategory());
        return productRepository.save(product);
    }

    public Mono<Product> update(Long id, UpdateProductRequest request) {
        return findById(id).flatMap(existing -> {
            if (request.name() != null)          existing.setName(request.name());
            if (request.description() != null)   existing.setDescription(request.description());
            if (request.category() != null)      existing.setCategory(request.category());
            if (request.price() != null)         existing.setPrice(request.price());
            if (request.currency() != null)      existing.setCurrency(request.currency());
            if (request.stockQuantity() != null) existing.setStockQuantity(request.stockQuantity());
            if (request.imageUrl() != null)      existing.setImageUrl(request.imageUrl());
            if (request.active() != null)        existing.setActive(request.active());
            existing.setUpdatedAt(LocalDateTime.now());

            log.info("Updating product: id={}", id);
            return productRepository.save(existing);
        });
    }

    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(product -> {
                    log.info("Deleting product: id={}", id);
                    return productRepository.deleteById(id);
                });
    }
}