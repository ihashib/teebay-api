package com.teebay.teebayapi.graphql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.UUID;


class ProductGraphQlTest extends GraphQlTestConfig {

    @Autowired HttpGraphQlTester tester;
    private HttpGraphQlTester authTester;

    @BeforeEach
    void setUp() {
        authTester = TestUtils.withAuth(tester);
    }

    @Test
    void testCreateUpdateDeleteProduct() {
        // Create
        String createDoc = """
          mutation {
            createProduct(input:{
              title:"Bike",
              description:"Road bike",
              categories:[OUTDOOR,SPORTING_GOODS],
              price:150.0,
              rentPrice:10.0,
              rentUnit:DAY
            }) {
              id title owner { email }
            }
          }
        """;

        var createResp = authTester.document(createDoc)
                .execute()
                .path("createProduct.id").entity(String.class).satisfies(id -> UUID.fromString(id))
                .path("createProduct.title").entity(String.class).isEqualTo("Bike")
                .path("createProduct.owner.email").hasValue();

        String productId = createResp.path("createProduct.id").entity(String.class).get();

        // Update
        String updateDoc = """
          mutation {
            updateProduct(input:{
              id:"%s",
              title:"Bike Pro",
              description:"Upgraded",
              categories:[OUTDOOR],
              price:175.0,
              rentPrice:12.0,
              rentUnit:DAY
            }) {
              id title description
            }
          }
        """.formatted(productId);

        authTester.document(updateDoc)
                .execute()
                .path("updateProduct.title").entity(String.class).isEqualTo("Bike Pro")
                .path("updateProduct.description").entity(String.class).isEqualTo("Upgraded");

        // Delete
        String deleteDoc = """
          mutation {
            deleteProduct(productId:"%s")
          }
        """.formatted(productId);

        authTester.document(deleteDoc)
                .execute()
                .path("deleteProduct").entity(Boolean.class).isEqualTo(true);
    }

    @Test
    void testQueryProducts() {
        // All products list
        tester.document("{ products { id title } }")
                .execute()
                .path("products").entityList(Object.class).hasSizeGreaterThan(0);

        // User products (requires auth but might be empty)
        // list may be empty or not, but query must succeed
        authTester.document("{ myProducts { id title } }")
                .execute()
                .path("myProducts").entityList(Object.class).satisfies(Assertions::assertNotNull);
    }
}
