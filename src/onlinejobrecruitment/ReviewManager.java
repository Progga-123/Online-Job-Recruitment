/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages company reviews
 */
public class ReviewManager {
    
    /**
     * Create reviews table
     */
    public static void createReviewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS company_review (" +
                     "review_id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "seeker_id INT NOT NULL, " +
                     "company_id INT NOT NULL, " +
                     "rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5), " +
                     "review_text TEXT, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (seeker_id) REFERENCES jobseeker(seeker_id) ON DELETE CASCADE, " +
                     "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Company review table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating review table: " + e.getMessage());
        }
    }
    
    /**
     * Add a review
     */
    public static boolean addReview(int seekerId, int companyId, int rating, String reviewText) {
        String sql = "INSERT INTO company_review (seeker_id, company_id, rating, review_text) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setInt(2, companyId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, reviewText);
            pstmt.executeUpdate();
            
            System.out.println("Review added successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding review: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all reviews for a company
     */
    public static List<CompanyReview> getCompanyReviews(int companyId) {
        List<CompanyReview> reviews = new ArrayList<>();
        
        String sql = "SELECT r.*, js.name as seeker_name " +
                     "FROM company_review r " +
                     "JOIN jobseeker js ON r.seeker_id = js.seeker_id " +
                     "WHERE r.company_id = ? " +
                     "ORDER BY r.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CompanyReview review = new CompanyReview();
                review.setReviewId(rs.getInt("review_id"));
                review.setSeekerId(rs.getInt("seeker_id"));
                review.setSeekerName(rs.getString("seeker_name"));
                review.setCompanyId(rs.getInt("company_id"));
                review.setRating(rs.getInt("rating"));
                review.setReviewText(rs.getString("review_text"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews: " + e.getMessage());
        }
        
        return reviews;
    }
    
    /**
     * Get average rating for a company
     */
    public static double getAverageRating(int companyId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM company_review WHERE company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            System.out.println("Error getting average rating: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Get review count for a company
     */
    public static int getReviewCount(int companyId) {
        String sql = "SELECT COUNT(*) FROM company_review WHERE company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting review count: " + e.getMessage());
        }
        return 0;
    }
}