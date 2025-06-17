package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.exception.UnauthorizedException;
import com.teebay.teebayapi.service.ProductService;
import com.teebay.teebayapi.service.UserService;
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
public class UserQueryResolver {
    private final UserService userService;

    @QueryMapping
    public User getCurrentUser(DataFetchingEnvironment env) {
        log.info("get product by id query called");

        UUID userId = env.getGraphQlContext().get("userId");

        return userService.getUserById(userId);
    }
}
