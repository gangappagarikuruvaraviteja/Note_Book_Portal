package com.notebook.portal.service;

import com.notebook.portal.dto.review.VerificationRequest;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.User;
import com.notebook.portal.entity.Verification;
import com.notebook.portal.exception.ResourceNotFoundException;
import com.notebook.portal.repository.NotebookRepository;
import com.notebook.portal.repository.VerificationRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final NotebookRepository notebookRepository;

    public void verifyNotebook(Notebook notebook, User faculty, VerificationRequest request) {
        Optional<Verification> existing = verificationRepository.findByNotebookId(notebook.getId());
        Verification verification = existing.orElseGet(Verification::new);
        verification.setNotebook(notebook);
        verification.setFaculty(faculty);
        verification.setStatus(request.getStatus());
        verification.setComment(request.getComment());
        verificationRepository.save(verification);

        notebook.setVerified(request.getStatus().name().equals("APPROVED"));
        notebookRepository.save(notebook);
    }

    public Verification getVerification(Long notebookId) {
        return verificationRepository.findByNotebookId(notebookId)
            .orElseThrow(() -> new ResourceNotFoundException("Verification not found"));
    }
}
