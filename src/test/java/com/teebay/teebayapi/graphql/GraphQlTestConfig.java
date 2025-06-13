package com.teebay.teebayapi.graphql;

import com.teebay.teebayapi.controller.*;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;

@GraphQlTest({
        UserMutationResolver.class,
        ProductMutationResolver.class,
        ProductQueryResolver.class,
        OrderMutationResolver.class,
        OrderQueryResolver.class
})
@AutoConfigureHttpGraphQlTester
public class GraphQlTestConfig {}
