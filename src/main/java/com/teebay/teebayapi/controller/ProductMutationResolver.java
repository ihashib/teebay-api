package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.exception.UnauthorizedException;
import com.teebay.teebayapi.service.ProductService;
import com.teebay.teebayapi.service.dto.ProductDto;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductMutationResolver {
    private final ProductService productService;

    @MutationMapping
    public Product createProduct(@Argument("input") @Valid ProductDto productDto, DataFetchingEnvironment env) {
        log.info("createProduct mutation called");

        // verify login
        UUID userId = env.getGraphQlContext().get("userId");
        if (userId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return productService.createProduct(userId, productDto);
    }

    @MutationMapping
    public Product updateProduct(@Argument("input") @Valid ProductDto productDto, DataFetchingEnvironment env) {
        log.info("updateProduct mutation called");

        // verify login
        UUID userId = env.getGraphQlContext().get("userId");
        if (userId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return productService.updateProduct(userId, productDto);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument UUID productId, DataFetchingEnvironment env) {
        log.info("deleteProduct mutation called");

        // verify login
        UUID userId = env.getGraphQlContext().get("userId");
        if (userId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return productService.deleteProduct(userId, productId);
    }
}
