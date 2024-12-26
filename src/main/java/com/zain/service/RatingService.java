package com.zain.service;

import com.zain.Request.RatingRequest;
import com.zain.exception.ProductException;
import com.zain.model.Rating;
import com.zain.model.User;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest req, User user) throws ProductException;
    public List<Rating> getProductsRatings(Long productId);

}
