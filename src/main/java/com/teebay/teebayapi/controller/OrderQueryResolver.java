package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.exception.UnauthorizedException;
import com.teebay.teebayapi.service.OrderService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderQueryResolver {

    private final OrderService orderService;

    @QueryMapping
    public List<Order> buyerOrders(DataFetchingEnvironment env) {
        log.info("get buyer orders query called");

        // verify login
        UUID buyerId = env.getGraphQlContext().get("userId");
        if (buyerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getBuyerAllOrders(buyerId);
    }

    @QueryMapping
    public List<Order> buyerBoughtOrders(DataFetchingEnvironment env) {
        log.info("get buyer bought orders query called");

        // verify login
        UUID buyerId = env.getGraphQlContext().get("userId");
        if (buyerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getBuyerBoughtOrders(buyerId);
    }

    @QueryMapping
    public List<Order> buyerRentedOrders(DataFetchingEnvironment env) {
        log.info("get buyer rented orders query called");

        // verify login
        UUID buyerId = env.getGraphQlContext().get("userId");
        if (buyerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getBuyerBorrowedOrders(buyerId);
    }

    @QueryMapping
    public List<Order> ownerSoldOrders(DataFetchingEnvironment env) {
        log.info("get owner sold all orders query called");

        // verify login
        UUID ownerId = env.getGraphQlContext().get("userId");
        if (ownerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getOwnerAllOrders(ownerId);
    }

    @QueryMapping
    public List<Order> ownerSoldBoughtOrders(DataFetchingEnvironment env) {
        log.info("get owner sold bought orders query called");

        // verify login
        UUID ownerId = env.getGraphQlContext().get("userId");
        if (ownerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getOwnerSoldOrders(ownerId);
    }

    @QueryMapping
    public List<Order> ownerSoldRentedOrders(DataFetchingEnvironment env) {
        log.info("get owner sold rented orders query called");

        // verify login
        UUID ownerId = env.getGraphQlContext().get("userId");
        if (ownerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        return orderService.getOwnerRentedOrders(ownerId);
    }
}
