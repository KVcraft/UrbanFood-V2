package com.urban_food.controller;

import com.urban_food.document.Review;
import com.urban_food.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/customer/{customerId}")
    public List<Review> getByCustomerId(@PathVariable String customerId) {
        return reviewService.getReviewsByCustomerId(customerId);
    }

    @GetMapping("/product/{productId}")
    public List<Review> getByProductId(@PathVariable String productId) {
        return reviewService.getReviewsByProductId(productId);
    }
}
