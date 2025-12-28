/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.Timestamp;

/**
 * Model class for company reviews
 */
public class CompanyReview {
    private int reviewId;
    private int seekerId;
    private String seekerName;
    private int companyId;
    private String companyName;
    private int rating; // 1-5 stars
    private String reviewText;
    private Timestamp createdAt;
    
    public CompanyReview() {}
    
    public CompanyReview(int seekerId, String seekerName, int companyId, 
                        int rating, String reviewText) {
        this.seekerId = seekerId;
        this.seekerName = seekerName;
        this.companyId = companyId;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    
    // Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    
    public String getSeekerName() { return seekerName; }
    public void setSeekerName(String seekerName) { this.seekerName = seekerName; }
    
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getStarRating() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("⭐");
        }
        return stars.toString();
    }
}