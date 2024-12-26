package com.zain.controller;

import com.zain.Request.RatingRequest;
import com.zain.exception.ProductException;
import com.zain.exception.UserException;
import com.zain.model.Rating;
import com.zain.model.User;
import com.zain.service.RatingService;
import com.zain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(req, user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRating(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        List<Rating> ratings = ratingService.getProductsRatings(productId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

}
