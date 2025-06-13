package com.teebay.teebayapi.graphql;

import org.springframework.graphql.test.tester.HttpGraphQlTester;
import java.util.UUID;

public class TestUtils {
    public static final String AUTH_TOKEN = "d1ea0a2c-29d0-4dd7-b0bd-2a86c0eaffaf";

    public static HttpGraphQlTester withAuth(HttpGraphQlTester tester) {
        return tester.mutate()
                .header("Authorization", AUTH_TOKEN)
                .build();
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
