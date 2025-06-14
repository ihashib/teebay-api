package com.teebay.teebayapi.graphql;

import com.teebay.teebayapi.controller.*;
import com.teebay.teebayapi.service.OrderService;
import com.teebay.teebayapi.service.ProductService;
import com.teebay.teebayapi.service.UserService;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;

@GraphQlTest({
        UserMutationResolver.class,
        ProductMutationResolver.class,
        ProductQueryResolver.class,
        OrderMutationResolver.class,
        OrderQueryResolver.class
})
@AutoConfigureHttpGraphQlTester
@Import({ UserService.class, ProductService.class, OrderService.class })
public class GraphQlTestConfig {}
