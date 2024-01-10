package io.quarkiverse.pdfbox.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PdfBoxResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/pdfbox")
                .then()
                .statusCode(200)
                .body(is("Hello pdfbox"));
    }
}
