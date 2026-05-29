package com.notebook.portal.controller;

import com.notebook.portal.dto.notebook.NotebookCreateRequest;
import com.notebook.portal.dto.notebook.NotebookResponse;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.User;
import com.notebook.portal.exception.BadRequestException;
import com.notebook.portal.repository.UserRepository;
import com.notebook.portal.service.NotebookService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/notebooks")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    public ResponseEntity<NotebookResponse> uploadNotebook(
        @RequestParam("file") MultipartFile file,
        @Valid NotebookCreateRequest request,
        Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(notebookService.uploadNotebook(file, request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotebookResponse> getNotebook(@PathVariable Long id) {
        return ResponseEntity.ok(notebookService.getNotebook(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NotebookResponse>> search(
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) String semester,
        @RequestParam(required = false) String branch,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int size,
        @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts.length > 0 ? sortParts[0] : "createdAt";
        String sortDir = sortParts.length > 1 ? sortParts[1] : "desc";
        Sort sortBy = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        PageRequest pageable = PageRequest.of(page, size, sortBy);
        return ResponseEntity.ok(notebookService.search(subject, semester, branch, keyword, pageable));
    }

    @PostMapping("/{id}/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> recordDownload(@PathVariable Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Notebook notebook = notebookService.getNotebookEntity(id);
        notebookService.recordDownload(notebook, user);
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    @GetMapping("/{id}/ai/summary")
    public ResponseEntity<Map<String, String>> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("summary", notebookService.getSummary(id)));
    }

    @GetMapping("/{id}/ai/questions")
    public ResponseEntity<Map<String, String>> getQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("questions", notebookService.getQuestions(id)));
    }

    @GetMapping("/{id}/ai/tags")
    public ResponseEntity<Map<String, String>> getTags(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("tags", notebookService.getTags(id)));
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("Authentication required");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
