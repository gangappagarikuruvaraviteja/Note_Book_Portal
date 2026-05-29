package com.notebook.portal.repository;

import com.notebook.portal.entity.Verification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByNotebookId(Long notebookId);
}
