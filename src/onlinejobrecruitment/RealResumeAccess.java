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
 * Real Subject - Performs actual database operations for resume access
 * This class does the real work without any access control
 */
public class RealResumeAccess implements ResumeAccess {
    
    @Override
    public List<Resume> getApplicationsByCompany(int companyId) {
        List<Resume> resumes = new ArrayList<>();
        
        String sql = "SELECT a.application_id, a.seeker_id, a.job_id, a.resume, " +
                     "a.status, a.application_date, " +
                     "js.name as seeker_name, j.title as job_title, j.company_id " +
                     "FROM application a " +
                     "JOIN jobseeker js ON a.seeker_id = js.seeker_id " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "WHERE j.company_id = ? " +
                     "ORDER BY a.application_date DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Resume resume = new Resume(
                    rs.getInt("application_id"),
                    rs.getInt("seeker_id"),
                    rs.getString("seeker_name"),
                    rs.getInt("job_id"),
                    rs.getString("job_title"),
                    rs.getInt("company_id"),
                    rs.getString("resume"),
                    rs.getString("status"),
                    rs.getString("application_date")
                );
                resumes.add(resume);
            }
            
            System.out.println("[RealResumeAccess] Fetched " + resumes.size() + 
                             " applications for company " + companyId);
            
        } catch (SQLException e) {
            System.out.println("Error fetching applications: " + e.getMessage());
        }
        
        return resumes;
    }
    
    @Override
    public Resume getResumeByApplicationId(int applicationId, int requestingCompanyId) {
        String sql = "SELECT a.application_id, a.seeker_id, a.job_id, a.resume, " +
                     "a.status, a.application_date, " +
                     "js.name as seeker_name, j.title as job_title, j.company_id " +
                     "FROM application a " +
                     "JOIN jobseeker js ON a.seeker_id = js.seeker_id " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "WHERE a.application_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Resume resume = new Resume(
                    rs.getInt("application_id"),
                    rs.getInt("seeker_id"),
                    rs.getString("seeker_name"),
                    rs.getInt("job_id"),
                    rs.getString("job_title"),
                    rs.getInt("company_id"),
                    rs.getString("resume"),
                    rs.getString("status"),
                    rs.getString("application_date")
                );
                
                System.out.println("[RealResumeAccess] Fetched resume for application " + applicationId);
                return resume;
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching resume: " + e.getMessage());
        }
        
        return null;
    }
    
    @Override
    public String getResumeContent(int applicationId, int requestingCompanyId) {
        Resume resume = getResumeByApplicationId(applicationId, requestingCompanyId);
        
        if (resume != null) {
            System.out.println("[RealResumeAccess] Retrieved resume content for application " + applicationId);
            return resume.getResumeContent();
        }
        
        return null;
    }
    
    @Override
    public boolean updateApplicationStatus(int applicationId, String newStatus, int companyId) {
        String sql = "UPDATE application a " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "SET a.status = ? " +
                     "WHERE a.application_id = ? AND j.company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, applicationId);
            pstmt.setInt(3, companyId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("[RealResumeAccess] Updated application " + applicationId + 
                                 " status to " + newStatus);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error updating application status: " + e.getMessage());
        }
        
        return false;
    }
}
