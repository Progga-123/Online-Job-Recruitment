/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.*;

/**
 * Manages premium features and payments for job seekers
 */
public class PremiumManager {
    
    /**
     * Create premium features table
     */
    public static void createPremiumTables() {
        // Premium features table
        String premiumSql = "CREATE TABLE IF NOT EXISTS premium_features (" +
                           "feature_id INT AUTO_INCREMENT PRIMARY KEY, " +
                           "seeker_id INT NOT NULL, " +
                           "feature_type VARCHAR(50) NOT NULL, " +
                           "payment_status VARCHAR(20) DEFAULT 'Not Paid', " +
                           "payment_amount DECIMAL(10, 2), " +
                           "activated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                           "UNIQUE KEY unique_feature (seeker_id, feature_type), " +
                           "FOREIGN KEY (seeker_id) REFERENCES jobseeker(seeker_id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(premiumSql);
            System.out.println("Premium features table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating premium table: " + e.getMessage());
        }
    }
    
    /**
     * Purchase a premium feature
     */
    public static boolean purchaseFeature(int seekerId, String featureType, double amount) {
        String sql = "INSERT INTO premium_features (seeker_id, feature_type, payment_status, payment_amount) " +
                     "VALUES (?, ?, 'Paid', ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setString(2, featureType);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
            
            System.out.println("Feature purchased: " + featureType + " for Seeker ID: " + seekerId);
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                System.out.println("Feature already purchased.");
                return false;
            }
            System.out.println("Error purchasing feature: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Check if seeker has a specific feature
     */
    public static boolean hasFeature(int seekerId, String featureType) {
        String sql = "SELECT COUNT(*) FROM premium_features " +
                     "WHERE seeker_id = ? AND feature_type = ? AND payment_status = 'Paid'";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setString(2, featureType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking feature: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Build JobSeekerComponent with appropriate decorators
     */
    public static JobSeekerComponent buildJobSeekerComponent(JobSeeker seeker) {
        // Start with basic job seeker
        JobSeekerComponent component = new BasicJobSeeker(seeker);
        
        // Add review feature if purchased
        if (hasFeature(seeker.getId(), "REVIEW_FEATURE")) {
            component = new ReviewFeatureDecorator(component);
        }
        
        // Add profile edit feature if purchased
        if (hasFeature(seeker.getId(), "PROFILE_EDIT")) {
            component = new ProfileEditDecorator(component);
        }
        
        return component;
    }
    
    /**
     * Get all features for a seeker
     */
    public static String getPremiumStatus(int seekerId) {
        StringBuilder status = new StringBuilder();
        
        String sql = "SELECT feature_type, payment_status, payment_amount, activated_date " +
                     "FROM premium_features WHERE seeker_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();
            
            boolean hasFeatures = false;
            while (rs.next()) {
                hasFeatures = true;
                status.append("✅ ").append(rs.getString("feature_type"))
                      .append(" - $").append(rs.getDouble("payment_amount"))
                      .append(" (").append(rs.getString("payment_status")).append(")\n");
            }
            
            if (!hasFeatures) {
                status.append("No premium features activated.");
            }
        } catch (SQLException e) {
            System.out.println("Error getting premium status: " + e.getMessage());
        }
        
        return status.toString();
    }
}