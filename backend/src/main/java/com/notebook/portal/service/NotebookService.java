package com.notebook.portal.service;

import com.notebook.portal.dto.notebook.NotebookCreateRequest;
import com.notebook.portal.dto.notebook.NotebookResponse;
import com.notebook.portal.entity.Category;
import com.notebook.portal.entity.Download;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.User;
import com.notebook.portal.exception.ResourceNotFoundException;
import com.notebook.portal.repository.CategoryRepository;
import com.notebook.portal.repository.DownloadRepository;
import com.notebook.portal.repository.NotebookRepository;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final CategoryRepository categoryRepository;
    private final DownloadRepository downloadRepository;
    private final FileStorageService fileStorageService;
    private final DocumentTextService documentTextService;
    private final AiService aiService;

    public NotebookResponse uploadNotebook(MultipartFile file, NotebookCreateRequest request, User user) {
        String fileUrl = fileStorageService.storeFile(file);
        Notebook notebook = new Notebook();
        notebook.setTitle(request.getTitle());
        notebook.setSubject(defaultValue(request.getSubject(), "General"));
        notebook.setSemester(defaultValue(request.getSemester(), "NA"));
        notebook.setBranch(defaultValue(request.getBranch(), "NA"));
        notebook.setDescription(request.getDescription());
        notebook.setContentText(trimText(documentTextService.extractText(file)));
        notebook.setFileUrl(fileUrl);
        notebook.setFileType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        notebook.setFileSize(file.getSize());
        notebook.setUploadedBy(user);
        notebook.setTags(parseCsv(request.getTags()));
        notebook.setCategories(resolveCategories(parseCsv(request.getCategories())));
        Notebook saved = notebookRepository.save(notebook);
        return toResponse(saved);
    }

    public NotebookResponse getNotebook(Long id) {
        return toResponse(getNotebookEntity(id));
    }

    public Page<NotebookResponse> search(String subject, String semester, String branch, String keyword, Pageable pageable) {
        return notebookRepository.search(
            normalize(subject),
            normalize(semester),
            normalize(branch),
            normalize(keyword),
            pageable
        ).map(this::toResponse);
    }

    public void recordDownload(Notebook notebook, User user) {
        Download download = new Download();
        download.setNotebook(notebook);
        download.setUser(user);
        downloadRepository.save(download);
        notebook.setDownloadsCount(notebook.getDownloadsCount() + 1);
        notebookRepository.save(notebook);
    }

    public String getSummary(Long notebookId) {
        Notebook notebook = getNotebookEntity(notebookId);
        return aiService.generateSummary(notebook, buildAiInput(notebook));
    }

    public String getQuestions(Long notebookId) {
        Notebook notebook = getNotebookEntity(notebookId);
        return aiService.generateQuestions(notebook, buildAiInput(notebook));
    }

    public String getTags(Long notebookId) {
        Notebook notebook = getNotebookEntity(notebookId);
        return aiService.generateTags(notebook, buildAiInput(notebook));
    }

    private String buildAiInput(Notebook notebook) {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(notebook.getTitle()).append("\n");
        builder.append("Subject: ").append(notebook.getSubject()).append("\n");
        builder.append("Semester: ").append(notebook.getSemester()).append("\n");
        builder.append("Branch: ").append(notebook.getBranch()).append("\n");
        if (notebook.getDescription() != null && !notebook.getDescription().isBlank()) {
            builder.append("Description: ").append(notebook.getDescription().trim()).append("\n");
        }
        String contentText = notebook.getContentText();
        if (contentText != null && !contentText.isBlank()) {
            builder.append("Notes: ").append(contentText.trim());
        }
        return builder.toString();
    }

    private String trimText(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        int limit = 12000;
        return trimmed.length() > limit ? trimmed.substring(0, limit) : trimmed;
    }

    public Notebook getNotebookEntity(Long id) {
        return notebookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notebook not found"));
    }

    private Set<String> parseCsv(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.split(","))
            .map(String::trim)
            .filter(item -> !item.isEmpty())
            .collect(Collectors.toSet());
    }

    private Set<Category> resolveCategories(Set<String> names) {
        return names.stream()
            .map(name -> categoryRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    return categoryRepository.save(category);
                })
            )
            .collect(Collectors.toSet());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String defaultValue(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
    }

    private NotebookResponse toResponse(Notebook notebook) {
        return NotebookResponse.builder()
            .id(notebook.getId())
            .title(notebook.getTitle())
            .subject(notebook.getSubject())
            .semester(notebook.getSemester())
            .branch(notebook.getBranch())
            .description(notebook.getDescription())
            .fileUrl(notebook.getFileUrl())
            .fileType(notebook.getFileType())
            .fileSize(notebook.getFileSize())
            .verified(notebook.isVerified())
            .ratingAvg(notebook.getRatingAvg())
            .ratingCount(notebook.getRatingCount())
            .downloadsCount(notebook.getDownloadsCount())
            .tags(notebook.getTags())
            .categories(notebook.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
            .uploadedByName(notebook.getUploadedBy().getName())
            .createdAt(notebook.getCreatedAt())
            .build();
    }
}
