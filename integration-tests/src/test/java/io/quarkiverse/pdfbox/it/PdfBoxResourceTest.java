package io.quarkiverse.pdfbox.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PdfBoxResourceTest {

    @Test
    public void should_read_pdf_contents() {
        given()
                .when().get("/pdfbox/read-contents")
                .then()
                .statusCode(200)
                .body(is("Hello PDFBox!"));
    }
}
