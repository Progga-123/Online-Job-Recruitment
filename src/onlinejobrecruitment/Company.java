/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Company class now implements JobSubject to manage subscribers
 * Updated to use Proxy Pattern for viewing applications
 */
public class Company extends User implements JobSubject {
    private String phone;
    private String location;
    private String website;
    private String detail;
    private List<JobObserver> observers;
    private ResumeAccess resumeAccess; // Using interface for flexibility
    
    public Company(String name, String email, String password, String phone, 
                   String location, String website, String detail) {
        super(name, email, password);
        this.phone = phone;
        this.location = location;
        this.website = website;
        this.detail = detail;
        this.observers = new ArrayList<>();
        this.resumeAccess = new ResumeAccessProxy(); // Using Proxy!
    }
    
    public Company(String email, String password) {
        super(null, email, password);
        this.observers = new ArrayList<>();
        this.resumeAccess = new ResumeAccessProxy(); // Using Proxy!
    }
    
    // Observer Pattern Implementation
    @Override
    public void attach(JobObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Subscriber added: Seeker ID " + observer.getObserverId());
        }
    }
    
    @Override
    public void detach(JobObserver observer) {
        observers.remove(observer);
        System.out.println("Subscriber removed: Seeker ID " + observer.getObserverId());
    }
    
    @Override
    public void notifyObservers(String jobTitle, int jobId) {
        System.out.println("\n📢 Notifying " + observers.size() + " subscribers about new job: " + jobTitle);
        for (JobObserver observer : observers) {
            observer.update(this.name, jobTitle, jobId);
        }
    }
    
    /**
     * Load all subscribers from database
     */
    public void loadSubscribers() {
        observers.clear();
        List<Integer> seekerIds = SubscriptionManager.getSubscribers(this.id);
        
        for (int seekerId : seekerIds) {
            JobSeeker seeker = loadJobSeekerById(seekerId);
            if (seeker != null) {
                observers.add(seeker);
            }
        }
        System.out.println("Loaded " + observers.size() + " subscribers for " + this.name);
    }
    
    /**
     * Helper method to load a JobSeeker by ID
     */
    private JobSeeker loadJobSeekerById(int seekerId) {
        String sql = "SELECT name, email FROM jobseeker WHERE seeker_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seekerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JobSeeker seeker = new JobSeeker(rs.getString("email"), "");
                seeker.id = seekerId;
                seeker.name = rs.getString("name");
                return seeker;
            }
        } catch (SQLException e) {
            System.out.println("Error loading seeker: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean register() {
        String sql = "INSERT INTO company (name, email, password, phone, location, website, detail) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phone);
            pstmt.setString(5, location);
            pstmt.setString(6, website);
            pstmt.setString(7, detail);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                System.out.println("Company registered successfully! Company ID: " + this.id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean login() {
        String sql = "SELECT company_id, name, phone, location, website, detail FROM company WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                this.id = rs.getInt("company_id");
                this.name = rs.getString("name");
                this.phone = rs.getString("phone");
                this.location = rs.getString("location");
                this.website = rs.getString("website");
                this.detail = rs.getString("detail");
                
                loadSubscribers();
                
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
    
    /**
     * Post a job and notify all subscribers
     */
    public boolean postJob(String title, int categoryId, String skills, String experience,
                          String salaryRange, Date deadline, int vacancy, String location,
                          String platform, String requirement) {
        String sql = "INSERT INTO job (company_id, category_id, title, skills, experience, " +
                     "salary_range, deadline, vacancy, posted_date, location, platform, requirement) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, this.id);
            pstmt.setInt(2, categoryId);
            pstmt.setString(3, title);
            pstmt.setString(4, skills);
            pstmt.setString(5, experience);
            pstmt.setString(6, salaryRange);
            pstmt.setDate(7, deadline);
            pstmt.setInt(8, vacancy);
            pstmt.setString(9, location);
            pstmt.setString(10, platform);
            pstmt.setString(11, requirement);
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            int jobId = -1;
            if (rs.next()) {
                jobId = rs.getInt(1);
            }
            
            System.out.println("Job posted successfully!");
            
            if (jobId != -1) {
                loadSubscribers();
                notifyObservers(title, jobId);
            }
            
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to post job: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * View applications using Proxy Pattern
     * The proxy handles authorization and logging automatically
     */
    public java.util.List<Resume> viewApplications() {
        System.out.println("\n=== Viewing Applications (via Proxy) ===");
        return resumeAccess.getApplicationsByCompany(this.id);
    }
    
    /**
     * View a specific resume/application
     */
    public Resume viewResume(int applicationId) {
        return resumeAccess.getResumeByApplicationId(applicationId, this.id);
    }
    
    /**
     * Get resume content (file/text)
     */
    public String getResumeContent(int applicationId) {
        return resumeAccess.getResumeContent(applicationId, this.id);
    }
    
    /**
     * Update application status (Accept/Reject)
     */
    public boolean updateApplicationStatus(int applicationId, String status) {
        return resumeAccess.updateApplicationStatus(applicationId, status, this.id);
    }
    
    /**
     * Get subscriber count
     */
    public int getSubscriberCount() {
        return SubscriptionManager.getSubscribers(this.id).size();
    }
    
    // Getters
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getWebsite() { return website; }
    public String getDetail() { return detail; }
}