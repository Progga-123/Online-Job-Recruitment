/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy Pattern Implementation - Protection Proxy
 * Controls access to resume data with:
 * 1. Authorization checking (only job owner can view applications)
 * 2. Access logging (tracks who accessed what and when)
 * 3. Lazy initialization (creates RealResumeAccess only when needed)
 */
public class ResumeAccessProxy implements ResumeAccess {
    
    private RealResumeAccess realResumeAccess;
    
    /**
     * Lazy initialization - only create real object when needed
     */
    private RealResumeAccess getRealAccess() {
        if (realResumeAccess == null) {
            realResumeAccess = new RealResumeAccess();
            System.out.println("[PROXY] Initialized RealResumeAccess");
        }
        return realResumeAccess;
    }
    
    @Override
    public List<Resume> getApplicationsByCompany(int companyId) {
        // Log the access attempt
        logAccess(companyId, -1, "VIEW_ALL_APPLICATIONS", true);
        
        System.out.println("[PROXY] Company " + companyId + " requesting all applications");
        
        // Forward to real object
        return getRealAccess().getApplicationsByCompany(companyId);
    }
    
    @Override
    public Resume getResumeByApplicationId(int applicationId, int requestingCompanyId) {
        System.out.println("[PROXY] Company " + requestingCompanyId + 
                         " requesting resume for application " + applicationId);
        
        // Authorization check - verify company owns the job
        if (!isAuthorized(applicationId, requestingCompanyId)) {
            System.out.println("[PROXY] ❌ ACCESS DENIED - Company " + requestingCompanyId + 
                             " does not own this job!");
            logAccess(requestingCompanyId, applicationId, "VIEW_RESUME", false);
            return null;
        }
        
        // Log successful access
        logAccess(requestingCompanyId, applicationId, "VIEW_RESUME", true);
        System.out.println("[PROXY] ✅ ACCESS GRANTED - Company authorized");
        
        // Forward to real object
        return getRealAccess().getResumeByApplicationId(applicationId, requestingCompanyId);
    }
    
    @Override
    public String getResumeContent(int applicationId, int requestingCompanyId) {
        System.out.println("[PROXY] Company " + requestingCompanyId + 
                         " requesting resume CONTENT for application " + applicationId);
        
        // Authorization check
        if (!isAuthorized(applicationId, requestingCompanyId)) {
            System.out.println("[PROXY] ❌ ACCESS DENIED - Unauthorized access attempt!");
            logAccess(requestingCompanyId, applicationId, "VIEW_CONTENT", false);
            return "❌ ACCESS DENIED: You do not have permission to view this resume.";
        }
        
        // Log successful access
        logAccess(requestingCompanyId, applicationId, "VIEW_CONTENT", true);
        System.out.println("[PROXY] ✅ ACCESS GRANTED - Retrieving content");
        
        // Forward to real object
        return getRealAccess().getResumeContent(applicationId, requestingCompanyId);
    }
    
    @Override
    public boolean updateApplicationStatus(int applicationId, String newStatus, int companyId) {
        System.out.println("[PROXY] Company " + companyId + 
                         " attempting to update application " + applicationId + 
                         " status to: " + newStatus);
        
        // Authorization check
        if (!isAuthorized(applicationId, companyId)) {
            System.out.println("[PROXY] ❌ ACCESS DENIED - Cannot update status!");
            logAccess(companyId, applicationId, "UPDATE_STATUS", false);
            return false;
        }
        
        // Log the update attempt
        logAccess(companyId, applicationId, "UPDATE_STATUS:" + newStatus, true);
        System.out.println("[PROXY] ✅ ACCESS GRANTED - Updating status");
        
        // Forward to real object
        return getRealAccess().updateApplicationStatus(applicationId, newStatus, companyId);
    }
    
    /**
     * Authorization Check - Verify company owns the job
     * This is the KEY security feature of the proxy
     */
    private boolean isAuthorized(int applicationId, int companyId) {
        String sql = "SELECT j.company_id FROM application a " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "WHERE a.application_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int jobOwnerCompanyId = rs.getInt("company_id");
                return jobOwnerCompanyId == companyId;
            }
            
        } catch (SQLException e) {
            System.out.println("[PROXY] Authorization check failed: " + e.getMessage());
        }
        
        return false; // Deny access by default
    }
    
    /**
     * Access Logging - Track all resume access attempts
     * Stores who accessed what, when, and whether it was successful
     */
    private void logAccess(int companyId, int applicationId, String action, boolean success) {
        String sql = "INSERT INTO resume_access_log " +
                     "(company_id, application_id, action, success, access_time) " +
                     "VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, applicationId);
            pstmt.setString(3, action);
            pstmt.setBoolean(4, success);
            pstmt.executeUpdate();
            
            System.out.println("[PROXY] Logged: Company " + companyId + 
                             " | Action: " + action + 
                             " | Success: " + (success ? "✅" : "❌"));
            
        } catch (SQLException e) {
            // Don't fail the operation if logging fails
            System.out.println("[PROXY] Failed to log access: " + e.getMessage());
        }
    }
}
