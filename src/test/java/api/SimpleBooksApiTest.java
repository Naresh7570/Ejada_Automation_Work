package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleBooksApiTest {

    private RequestSpecification authenticatedSpec;
    private String orderId;

    // ============================================
    // Setup Authenticated API Context
    // ============================================
    @BeforeClass
    public void setUp() {

        String baseUrl = ConfigReader.get("api.base.url");
        String clientName = ConfigReader.get("api.client.name");

        RestAssured.baseURI = baseUrl;

        // Authentication Payload
        Map<String, String> authBody = new HashMap<>();
        authBody.put("clientName", clientName);

        // API requires unique email every execution
        authBody.put(
                "clientEmail",
                "test" + System.currentTimeMillis() + "@mail.com"
        );

        // Generate Token
        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(authBody)
                .when()
                .post("/api-clients");

        authResponse.then().statusCode(201);

        String token = authResponse.jsonPath()
                .getString("accessToken");

        System.out.println("Generated Token: " + token);

        // Reusable Authenticated Spec
        authenticatedSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader(
                        "Authorization",
                        "Bearer " + token)
                .addHeader(
                        "Accept",
                        "application/json")
                .setContentType(ContentType.JSON)
                .build();
    }

    // ============================================
    // GET All Books
    // ============================================
    @Test(priority = 1)
    public void getAllBooks() {

        given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .log().body();
    }

    // ============================================
    // GET Single Book
    // ============================================
    @Test(priority = 2)
    public void getSingleBook() {

        given()
                .when()
                .get("/books/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .log().body();
    }

    // ============================================
    // Negative Test - Invalid Book ID
    // ============================================
    @Test(priority = 3)
    public void getInvalidBook() {

        given()
                .when()
                .get("/books/999")
                .then()
                .statusCode(404)
                .body(
                        "error",
                        containsString("No book with id"))
                .log().body();
    }

    // ============================================
    // Create Order
    // ============================================
    @Test(priority = 4)
    public void createOrder() {

        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("bookId", 1);
        orderBody.put("customerName", "Naveen");

        Response response = given()
                .spec(authenticatedSpec)
                .body(orderBody)
                .when()
                .post("/orders");

        response.then()
                .statusCode(201)
                .body("created", equalTo(true));

        orderId = response.jsonPath()
                .getString("orderId");

        System.out.println(
                "Created Order ID: " + orderId);
    }

    // ============================================
    // Get Orders
    // ============================================
    @Test(priority = 5)
    public void getOrders() {

        given()
                .spec(authenticatedSpec)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .log().body();
    }

    // ============================================
    // Update Order
    // ============================================
    @Test(priority = 6)
    public void updateOrder() {

        Map<String, String> updateBody = new HashMap<>();
        updateBody.put("customerName", "Updated User");

        given()
                .spec(authenticatedSpec)
                .body(updateBody)
                .when()
                .patch("/orders/" + orderId)
                .then()
                .statusCode(204);

        System.out.println(
                "Order Updated Successfully");
    }

    // ============================================
    // Delete Order
    // ============================================
    @Test(priority = 7)
    public void deleteOrder() {

        given()
                .spec(authenticatedSpec)
                .when()
                .delete("/orders/" + orderId)
                .then()
                .statusCode(204);

        System.out.println(
                "Order Deleted Successfully");
    }

    // ============================================
    // Verify Deleted Order
    // ============================================
    @Test(priority = 8)
    public void verifyDeletedOrder() {

        given()
                .spec(authenticatedSpec)
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(404);

        System.out.println(
                "Verified Deleted Order");
    }

    // ============================================
    // Negative Test - Invalid Token
    // ============================================
    @Test(priority = 9)
    public void accessOrdersWithInvalidToken() {

        given()
                .header(
                        "Authorization",
                        "Bearer invalid_token")
                .when()
                .get("/orders")
                .then()
                .statusCode(401)
                .body(
                        "error",
                        containsString(
                                "Invalid bearer token"))
                .log().body();
    }

    // ============================================
    // Negative Test - Missing Token
    // ============================================
    @Test(priority = 10)
    public void accessOrdersWithoutToken() {

        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(401)
                .body(
                        "error",
                        containsString(
                                "Missing Authorization header"))
                .log().body();
    }
}