package io.quarkiverse.pdfbox.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

    @Test
    public void should_create_pdf() throws Exception {
        byte[] body = given()
                .when().get("/pdfbox/create-pdf")
                .then()
                .statusCode(200)
                .contentType("application/pdf")
                .extract().asByteArray();

        PDDocument pdf = Loader.loadPDF(body);
        String text = new PDFTextStripper().getText(pdf).trim();
        assertEquals("Apache PDFBox Center Text PDF Document", text);
    }

    @Test
    public void should_split_pdf() throws Exception {
        given()
                .when().get("/pdfbox/split-pdf")
                .then()
                .statusCode(200)
                .body(equalTo("3"));
    }
}
