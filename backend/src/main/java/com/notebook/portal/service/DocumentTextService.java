package com.notebook.portal.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DocumentTextService {

    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "";
        }
        if (!isPdf(file)) {
            return "";
        }
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException ex) {
            log.warn("Failed to extract PDF text", ex);
            return "";
        }
    }

    private boolean isPdf(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && contentType.equalsIgnoreCase("application/pdf")) {
            return true;
        }
        String name = file.getOriginalFilename();
        return name != null && name.toLowerCase().endsWith(".pdf");
    }
}
