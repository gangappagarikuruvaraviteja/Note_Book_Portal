package com.notebook.portal.service;

import com.notebook.portal.dto.review.ReviewRequest;
import com.notebook.portal.entity.Notebook;
import com.notebook.portal.entity.Review;
import com.notebook.portal.entity.User;
import com.notebook.portal.repository.NotebookRepository;
import com.notebook.portal.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final NotebookRepository notebookRepository;

    public void addReview(Notebook notebook, User user, ReviewRequest request) {
        Review review = new Review();
        review.setNotebook(notebook);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        int count = notebook.getRatingCount() + 1;
        double total = notebook.getRatingAvg() * notebook.getRatingCount() + request.getRating();
        notebook.setRatingCount(count);
        notebook.setRatingAvg(total / count);
        notebookRepository.save(notebook);
    }
}
