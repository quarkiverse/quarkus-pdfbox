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

import java.io.InputStream;
import java.net.URL;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
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
}
