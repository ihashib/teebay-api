package com.teebay.teebayapi.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.UUID;

class UserGraphQlTest extends GraphQlTestConfig {

    @Autowired HttpGraphQlTester tester;

    @Test
    void testRegisterAndLogin() {
        String email = "user" + System.nanoTime() + "@testing.com";
        String registerDoc = """
          mutation {
            register(input:{
              email: "%s",
              password: "pass123",
              firstName: "Test",
              lastName: "User",
              address: "TB Gate, Dhaka 1212",
              phoneNumber: "01960174282",
              userType: USER
            }) {
              id email firstName
            }
          }
        """.formatted(email);

        // Register
        tester.document(registerDoc)
                .execute()
                .path("register.id").hasValue()
                .path("register.email").entity(String.class).isEqualTo(email);

        // Login
        String loginDoc = """
          mutation {
            login(input:{
              email: "%s",
              password: "pass123"
            })
          }
        """.formatted(email);

        tester.document(loginDoc)
                .execute()
                .path("login").entity(String.class).satisfies(id -> {
                    UUID.fromString(id);
                });
    }
}
