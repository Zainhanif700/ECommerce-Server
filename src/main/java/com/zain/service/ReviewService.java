package com.zain.service;

import com.zain.Request.ReviewRequest;
import com.zain.exception.ProductException;
import com.zain.model.Review;
import com.zain.model.User;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest req, User user) throws ProductException;
    public List<Review> getAllReviews(Long productId) throws ProductException;
}
