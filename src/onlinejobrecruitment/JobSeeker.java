/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JobSeeker class now implements JobObserver to receive notifications
 */
public class JobSeeker extends User implements JobObserver {
    private String phone;
    private String address;
    private String experience;
    private String skills;
    
    public JobSeeker(String name, String email, String password, String phone,
                     String address, String experience, String skills) {
        super(name, email, password);
        this.phone = phone;
        this.address = address;
        this.experience = experience;
        this.skills = skills;
    }
    
    public JobSeeker(String email, String password) {
        super(null, email, password);
    }
    
    // Observer Pattern Implementation
    @Override
    public void update(String companyName, String jobTitle, int jobId) {
        // Store notification in database
        NotificationManager.addNotification(this.id, companyName, jobTitle, jobId);
        System.out.println("✉ Notification sent to " + this.name + ": New job '" + 
                          jobTitle + "' posted by " + companyName);
    }
    
    @Override
    public int getObserverId() {
        return this.id;
    }
    
    /**
     * Subscribe to a company's job postings
     */
    public boolean subscribeToCompany(int companyId) {
        if (SubscriptionManager.addSubscription(this.id, companyId)) {
            System.out.println("Successfully subscribed to company updates!");
            return true;
        }
        return false;
    }
    
    /**
     * Unsubscribe from a company's job postings
     */
    public boolean unsubscribeFromCompany(int companyId) {
        if (SubscriptionManager.removeSubscription(this.id, companyId)) {
            System.out.println("Successfully unsubscribed from company.");
            return true;
        }
        return false;
    }
    
    /**
     * Get all notifications for this job seeker
     */
    public List<Notification> getNotifications() {
        return NotificationManager.getNotifications(this.id);
    }
    
    /**
     * Get unread notification count
     */
    public int getUnreadNotificationCount() {
        return NotificationManager.getUnreadCount(this.id);
    }
    
    /**
     * Mark all notifications as read
     */
    public void markAllNotificationsAsRead() {
        NotificationManager.markAllAsRead(this.id);
    }
    
    @Override
    public boolean register() {
        String sql = "INSERT INTO jobseeker (name, email, password, phone, address, experience, skills) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, experience);
            pstmt.setString(7, skills);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                System.out.println("Job Seeker registered successfully! Seeker ID: " + this.id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean login() {
        String sql = "SELECT seeker_id, name, phone, address, experience, skills " +
                     "FROM jobseeker WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                this.id = rs.getInt("seeker_id");
                this.name = rs.getString("name");
                this.phone = rs.getString("phone");
                this.address = rs.getString("address");
                this.experience = rs.getString("experience");
                this.skills = rs.getString("skills");
                
                System.out.println("Login successful! Welcome, " + name);
                return true;
            } else {
                System.out.println("Login failed: Invalid credentials");
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        return false;
    }
    
    public List<Job> searchJobs(String keyword) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.*, c.name as company_name FROM job j " +
                     "JOIN company c ON j.company_id = c.company_id " +
                     "WHERE j.title LIKE ? OR j.skills LIKE ? OR j.location LIKE ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Job job = new Job();
                job.setJobId(rs.getInt("job_id"));
                job.setTitle(rs.getString("title"));
                job.setCompanyName(rs.getString("company_name"));
                job.setLocation(rs.getString("location"));
                job.setSalaryRange(rs.getString("salary_range"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
        return jobs;
    }
    
    public boolean applyJob(int jobId, String resume) {
        String sql = "INSERT INTO application (seeker_id, job_id, resume, status, application_date) " +
                     "VALUES (?, ?, ?, 'Pending', CURDATE())";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, this.id);
            pstmt.setInt(2, jobId);
            pstmt.setString(3, resume);
            
            pstmt.executeUpdate();
            System.out.println("Application submitted successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Application failed: " + e.getMessage());
        }
        return false;
    }
    
    // Getters
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getExperience() { return experience; }
    public String getSkills() { return skills; }
}