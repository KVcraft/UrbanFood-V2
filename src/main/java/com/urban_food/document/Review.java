package com.urban_food.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;

    private String customerId;
    private String productId;
    private String comment;
    private int rating; // 1–5

    public Review() {}

    public Review(String customerId, String productId, String comment, int rating) {
        this.customerId = customerId;
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
