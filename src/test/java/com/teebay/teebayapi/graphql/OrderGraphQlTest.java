package com.teebay.teebayapi.graphql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.time.Instant;

class OrderGraphQlTest extends GraphQlTestConfig {

    @Autowired HttpGraphQlTester tester;
    private HttpGraphQlTester authTester;
    private String productId;

    @BeforeEach
    void setUp() {
        authTester = TestUtils.withAuth(tester);
        // create a product to order
        var resp = authTester.document("""
          mutation {
            createProduct(input:{
              title:"Yoyo",
              description:"Kids yoyo toy",
              categories:[TOYS],
              price:20.0,
              rentPrice:2.0,
              rentUnit:DAY
            }) { id }
          }
        """).execute();
        productId = resp.path("createProduct.id").entity(String.class).get();
    }

    @Test
    void testBuyProduct() {
        var buyResp = authTester.document("""
          mutation {
            buyProduct(id:"%s") {
              id type
            }
          }
        """.formatted(productId))
                .execute()
                .path("buyProduct.type").entity(String.class).isEqualTo("BUY");

        String orderId = buyResp.path("buyProduct.id").entity(String.class).get();
        // buyerBoughtOrders should include this
        authTester.document("""
          { myBoughtOrders { id } }
        """).execute()
                .path("myBoughtOrders[*].id").entityList(String.class)
                .contains(orderId);
    }

    @Test
    void testRentProduct() {
        String from = Instant.now().toString();
        String to   = Instant.now().plusSeconds(86400).toString();

        var rentResp = authTester.document("""
          mutation {
            rentProduct(id:"%s", from:"%s", to:"%s") {
              id type rentStart rentEnd
            }
          }
        """.formatted(productId, from, to))
                .execute()
                .path("rentProduct.type").entity(String.class).isEqualTo("RENT")
                .path("rentProduct.rentStart").entity(String.class).isEqualTo(from)
                .path("rentProduct.rentEnd").entity(String.class).isEqualTo(to);

        String rentOrderId = rentResp.path("rentProduct.id").entity(String.class).get();
        // myRentedOrders should include this
        authTester.document("{ myRentedOrders { id } }")
                .execute()
                .path("myRentedOrders[*].id").entityList(String.class)
                .contains(rentOrderId);
    }

    @Test
    void testSoldOrders() {
        // buy to generate a sold order
        authTester.document("mutation { buyProduct(id:\"%s\"){id}}".formatted(productId))
                .execute();

        authTester.document("{ soldOrders { id product { id } } }")
                .execute()
                .path("soldOrders[*].product.id").entityList(String.class)
                .contains(productId);
    }
}
