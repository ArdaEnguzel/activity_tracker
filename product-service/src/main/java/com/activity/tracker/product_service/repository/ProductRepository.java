package com.activity.tracker.product_service.repository;

import com.activity.tracker.product_service.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Flux<Product> findByCategory(String category);

    Flux<Product> findByActiveTrue();

    Flux<Product> findByCategoryAndActiveTrue(String category);
}