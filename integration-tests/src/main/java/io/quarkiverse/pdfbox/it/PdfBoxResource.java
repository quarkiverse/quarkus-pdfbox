/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.pdfbox.it;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripper;

@Path("/pdfbox")
@ApplicationScoped
public class PdfBoxResource {
    // add some rest methods here

    @GET
    @Path("/read-contents")
    public String readPDFContents() throws Exception {
        URL resource = getClass().getClassLoader().getResource("hello.pdf");
        final byte[] content;
        // Parse PDF using PDFBox
        try (InputStream inputStream = resource.openStream()) {
            content = IOUtils.toByteArray(inputStream);
        }
        PDDocument pdf = Loader.loadPDF(content);
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getText(pdf).trim();
    }

    @GET
    @Path("/create-pdf")
    @Produces("application/pdf")
    public byte[] createPDF() throws IOException {
        String title = "Apache PDFBox Center Text PDF Document";

        final PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(doc, page);

        stream.beginText();
        stream.newLineAtOffset(0, 0);
        PDFont font = PDType0Font.load(doc, getClass().getClassLoader().getResourceAsStream("Roboto-Bold.ttf"));
        stream.setFont(font, 12);
        stream.showText(title);
        stream.endText();
        stream.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.save(baos);
        return baos.toByteArray();
    }

    @GET
    @Path("/split-pdf")
    @Produces("text/plain")
    public int splitPDF() throws IOException {
        URL resource = getClass().getClassLoader().getResource("sample-tables.pdf");
        final byte[] content;
        // Parse PDF using PDFBox
        try (InputStream inputStream = resource.openStream()) {
            content = IOUtils.toByteArray(inputStream);
        }
        PDDocument pdf = Loader.loadPDF(content);
        Splitter splitter = new Splitter();
        splitter.setSplitAtPage(1);
        List<PDDocument> split = splitter.split(pdf);
        return split.size();
    }

    @POST
    @Path("/extract-markdown")
    @Produces("text/markdown")
    public String extractTextAsMarkdown(final byte[] file) throws IOException {
        try (final PDDocument document = Loader.loadPDF(file)) {
            final PDFTextStripper stripper = new PDFTextStripper();
            final String rawText = stripper.getText(document);

            final String markdown = rawText
                    .replaceAll("\r\n?", "\n") // normalize \r\n or \r to \n
                    .replaceAll("(?m)^\\s*(\\d+\\.)", "- $1") // numbered lists to bullets
                    .replaceAll("(?m)^\\s*([A-Z][A-Z ]{3,})$", "## $1") // uppercase headings to markdown headings
                    .replaceAll("\n{2,}", "\n\n"); // normalize spacing

            return markdown.trim();
        }
    }

}
