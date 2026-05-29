package com.notebook.portal.service;

import com.notebook.portal.exception.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadPath = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException ex) {
            throw new BadRequestException("Unable to create upload directory");
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + (originalName == null ? "file" : originalName);
        Path target = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file");
        }
        return "/api/files/" + fileName;
    }

    public Path resolveFile(String filename) {
        return uploadPath.resolve(filename).normalize();
    }
}
