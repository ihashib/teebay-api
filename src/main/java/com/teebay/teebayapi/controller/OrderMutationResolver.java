package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import com.teebay.teebayapi.exception.UnauthorizedException;
import com.teebay.teebayapi.service.OrderService;
import com.teebay.teebayapi.service.dto.OrderRequestDto;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OrderMutationResolver {

    private final OrderService orderService;

    @MutationMapping
    public Order buyProduct(@Argument("id") UUID productId, DataFetchingEnvironment env) {
        // verify login
        UUID buyerId = env.getGraphQlContext().get("userId");
        if (buyerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        // build OrderRequestDto to buy product
        OrderRequestDto req = new OrderRequestDto()
                .setBuyerId(buyerId)
                .setProductId(productId)
                .setType(OrderType.BUY);

        return orderService.orderProduct(req);
    }

    @MutationMapping
    public Order rentProduct(@Argument("id") UUID productId,
                             @Argument("from") String from,
                             @Argument("to") String to,
                             DataFetchingEnvironment env) {
        // verify login
        UUID buyerId = env.getGraphQlContext().get("userId");
        if (buyerId == null) {
            throw new UnauthorizedException("User not logged in");
        }

        // convert to instant
        Instant rentStart = Instant.parse(from);
        Instant rentEnd = Instant.parse(to);

        // build orderRequestDto to rent
        OrderRequestDto req = new OrderRequestDto()
                .setBuyerId(buyerId)
                .setProductId(productId)
                .setType(OrderType.RENT)
                .setRentStart(rentStart)
                .setRentEnd(rentEnd);

        return orderService.orderProduct(req);
    }
}
