package com.notebook.portal.repository;

import com.notebook.portal.entity.AiResult;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiResultRepository extends JpaRepository<AiResult, Long> {
    Optional<AiResult> findTopByNotebookIdAndTypeOrderByCreatedAtDesc(Long notebookId, String type);
}
