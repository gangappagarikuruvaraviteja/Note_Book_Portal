package com.notebook.portal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OcrService {

    @Value("${app.ocr.enabled:false}")
    private boolean ocrEnabled;

    public String extractText(byte[] bytes) {
        if (!ocrEnabled) {
            return "OCR disabled. Set OCR_ENABLED=true to enable.";
        }
        return "OCR placeholder text.";
    }
}
