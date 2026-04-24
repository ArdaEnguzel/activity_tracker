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
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        log.info("Creating product: name={} category={}", product.getName(), product.getCategory());
        return productRepository.save(product);
    }

    public Mono<Product> update(Long id, UpdateProductRequest request) {
        return findById(id).flatMap(existing -> {
            if (request.getName() != null)          existing.setName(request.getName());
            if (request.getDescription() != null)   existing.setDescription(request.getDescription());
            if (request.getCategory() != null)      existing.setCategory(request.getCategory());
            if (request.getPrice() != null)         existing.setPrice(request.getPrice());
            if (request.getCurrency() != null)      existing.setCurrency(request.getCurrency());
            if (request.getStockQuantity() != null) existing.setStockQuantity(request.getStockQuantity());
            if (request.getImageUrl() != null)      existing.setImageUrl(request.getImageUrl());
            if (request.getActive() != null)        existing.setActive(request.getActive());
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