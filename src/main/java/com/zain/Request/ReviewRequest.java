package com.zain.Request;

public class ReviewRequest {
    private Long productId;
    private String review;

    public Long getProductId() {
        return productId;
    }

    public ReviewRequest(Long productId, String review) {
        this.productId = productId;
        this.review = review;
    }

    public ReviewRequest() {
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
