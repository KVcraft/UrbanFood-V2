package com.urban_food.repository;

import com.urban_food.document.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByCustomerId(String customerId);
    List<Review> findByProductId(String productId);
}
