package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.exception.UnauthorizedException;
import com.teebay.teebayapi.service.ProductService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductQueryResolver {
    private final ProductService productService;

    @QueryMapping
    public List<Product> products() {
        log.info("get all product query called");

        return productService.getAllProducts();
    }

    @QueryMapping
    public List<Product> userProducts(DataFetchingEnvironment env) {
        log.info("get user's all products query called");

        // verify login
        UUID userId = env.getGraphQlContext().get("userId");
        if (userId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return productService.getProductsByOwner(userId);
    }

    @QueryMapping
    public Product productById(@Argument("productId") UUID productId) {
        log.info("get product by id query called");

        return productService.getProductById(productId);
    }
}
