package com.notebook.portal.controller;

import com.notebook.portal.dto.review.VerificationRequest;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.User;
import com.notebook.portal.exception.BadRequestException;
import com.notebook.portal.repository.UserRepository;
import com.notebook.portal.service.NotebookService;
import com.notebook.portal.service.VerificationService;
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
public class VerificationController {

    private final NotebookService notebookService;
    private final VerificationService verificationService;
    private final UserRepository userRepository;

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    public ResponseEntity<Void> verifyNotebook(
        @PathVariable Long id,
        @Valid @RequestBody VerificationRequest request,
        Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        Notebook notebook = notebookService.getNotebookEntity(id);
        verificationService.verifyNotebook(notebook, user, request);
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
