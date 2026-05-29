package com.notebook.portal.controller;

import com.notebook.portal.dto.review.ReviewRequest;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.User;
import com.notebook.portal.exception.BadRequestException;
import com.notebook.portal.repository.UserRepository;
import com.notebook.portal.service.NotebookService;
import com.notebook.portal.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notebooks")
@RequiredArgsConstructor
public class ReviewController {

    private final NotebookService notebookService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> addReview(
        @PathVariable Long id,
        @Valid @RequestBody ReviewRequest request,
        Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        Notebook notebook = notebookService.getNotebookEntity(id);
        reviewService.addReview(notebook, user, request);
        return ResponseEntity.ok().build();
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
