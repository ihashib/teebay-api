package com.teebay.teebayapi.repository;

import com.teebay.teebayapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByOwnerId(UUID ownerId);
}
