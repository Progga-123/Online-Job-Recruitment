/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages notifications in the database
 */
public class NotificationManager {
    
    /**
     * Create the notification table if it doesn't exist
     */
    public static void createNotificationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notification (" +
                     "notification_id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "seeker_id INT NOT NULL, " +
                     "company_name VARCHAR(255), " +
                     "job_title VARCHAR(255), " +
                     "job_id INT, " +
                     "message TEXT, " +
                     "is_read BOOLEAN DEFAULT FALSE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (seeker_id) REFERENCES jobseeker(seeker_id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Notification table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating notification table: " + e.getMessage());
        }
    }
    
    /**
     * Add a notification to the database
     */
    public static boolean addNotification(int seekerId, String companyName, 
                                         String jobTitle, int jobId) {
        String message = "New job posted by " + companyName + ": " + jobTitle;
        String sql = "INSERT INTO notification (seeker_id, company_name, job_title, job_id, message) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.setString(2, companyName);
            pstmt.setString(3, jobTitle);
            pstmt.setInt(4, jobId);
            pstmt.setString(5, message);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding notification: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all notifications for a job seeker
     */
    public static List<Notification> getNotifications(int seekerId) {
        List<Notification> notifications = new ArrayList<Notification>();
        String sql = "SELECT * FROM notification WHERE seeker_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Notification notif = new Notification();
                notif.setNotificationId(rs.getInt("notification_id"));
                notif.setSeekerId(rs.getInt("seeker_id"));
                notif.setCompanyName(rs.getString("company_name"));
                notif.setJobTitle(rs.getString("job_title"));
                notif.setJobId(rs.getInt("job_id"));
                notif.setMessage(rs.getString("message"));
                notif.setRead(rs.getBoolean("is_read"));
                notif.setCreatedAt(rs.getTimestamp("created_at"));
                notifications.add(notif);
            }
        } catch (SQLException e) {
            System.out.println("Error getting notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get unread notification count
     */
    public static int getUnreadCount(int seekerId) {
        String sql = "SELECT COUNT(*) FROM notification WHERE seeker_id = ? AND is_read = FALSE";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting unread count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Mark a notification as read
     */
    public static boolean markAsRead(int notificationId) {
        String sql = "UPDATE notification SET is_read = TRUE WHERE notification_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error marking notification as read: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark all notifications as read for a seeker
     */
    public static boolean markAllAsRead(int seekerId) {
        String sql = "UPDATE notification SET is_read = TRUE WHERE seeker_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error marking all as read: " + e.getMessage());
        }
        return false;
    }
}