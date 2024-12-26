package com.zain.controller;

import com.zain.Request.RatingRequest;
import com.zain.Request.ReviewRequest;
import com.zain.exception.ProductException;
import com.zain.exception.UserException;
import com.zain.model.Rating;
import com.zain.model.Review;
import com.zain.model.User;
import com.zain.repository.ReviewRepository;
import com.zain.service.RatingService;
import com.zain.service.ReviewService;
import com.zain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReview(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        List<Review> review = reviewService.getAllReviews(productId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

}
