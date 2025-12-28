package onlinejobrecruitment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages subscriptions in the database
 * Creates and manages the subscription table
 */
public class SubscriptionManager {
    
    /**
     * Create the subscription table if it doesn't exist
     */
    public static void createSubscriptionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS subscription (" +
                     "subscription_id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "seeker_id INT NOT NULL, " +
                     "company_id INT NOT NULL, " +
                     "subscribed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "UNIQUE KEY unique_subscription (seeker_id, company_id), " +
                     "FOREIGN KEY (seeker_id) REFERENCES jobseeker(seeker_id) ON DELETE CASCADE, " +
                     "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Subscription table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating subscription table: " + e.getMessage());
        }
    }
    
    /**
     * Add a subscription to the database
     */
    public static boolean addSubscription(int seekerId, int companyId) {
        String sql = "INSERT INTO subscription (seeker_id, company_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setInt(2, companyId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                System.out.println("Already subscribed to this company.");
            } else {
                System.out.println("Error adding subscription: " + e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Remove a subscription from the database
     */
    public static boolean removeSubscription(int seekerId, int companyId) {
        String sql = "DELETE FROM subscription WHERE seeker_id = ? AND company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setInt(2, companyId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error removing subscription: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all seeker IDs subscribed to a specific company
     */
    public static List<Integer> getSubscribers(int companyId) {
        List<Integer> seekerIds = new ArrayList<Integer>();
        String sql = "SELECT seeker_id FROM subscription WHERE company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                seekerIds.add(rs.getInt("seeker_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting subscribers: " + e.getMessage());
        }
        return seekerIds;
    }
    
    /**
     * Get all companies a seeker is subscribed to
     */
    public static List<Integer> getSubscribedCompanies(int seekerId) {
        List<Integer> companyIds = new ArrayList<Integer>();
        String sql = "SELECT company_id FROM subscription WHERE seeker_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                companyIds.add(rs.getInt("company_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting subscribed companies: " + e.getMessage());
        }
        return companyIds;
    }
    
    /**
     * Check if a seeker is subscribed to a company
     */
    public static boolean isSubscribed(int seekerId, int companyId) {
        String sql = "SELECT COUNT(*) FROM subscription WHERE seeker_id = ? AND company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setInt(2, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking subscription: " + e.getMessage());
        }
        return false;
    }
}