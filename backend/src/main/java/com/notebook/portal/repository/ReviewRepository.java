package com.notebook.portal.repository;

import com.notebook.portal.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByNotebookId(Long notebookId);
}
