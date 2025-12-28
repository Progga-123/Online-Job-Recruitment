/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages interview scheduling and status updates
 */
public class InterviewManager {
    
    /**
     * Create the interview table if it doesn't exist
     */
    public static void createInterviewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS interview (" +
                     "interview_id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "application_id INT NOT NULL, " +
                     "company_id INT NOT NULL, " +
                     "seeker_id INT NOT NULL, " +
                     "interview_date DATE NOT NULL, " +
                     "interview_time TIME NOT NULL, " +
                     "location VARCHAR(500), " +
                     "type VARCHAR(50) DEFAULT 'In-person', " +
                     "meeting_link VARCHAR(500), " +
                     "notes TEXT, " +
                     "status VARCHAR(50) DEFAULT 'Scheduled', " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (application_id) REFERENCES application(application_id) ON DELETE CASCADE, " +
                     "FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE, " +
                     "FOREIGN KEY (seeker_id) REFERENCES jobseeker(seeker_id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Interview table ready.");
        } catch (SQLException e) {
            System.out.println("Error creating interview table: " + e.getMessage());
        }
    }
    
    /**
     * Schedule an interview for an application
     */
    public static boolean scheduleInterview(int applicationId, int companyId, int seekerId,
                                           String date, String time, String location,
                                           String type, String meetingLink, String notes) {
        String sql = "INSERT INTO interview (application_id, company_id, seeker_id, " +
                     "interview_date, interview_time, location, type, meeting_link, notes, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Scheduled')";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, applicationId);
            pstmt.setInt(2, companyId);
            pstmt.setInt(3, seekerId);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            pstmt.setString(6, location);
            pstmt.setString(7, type);
            pstmt.setString(8, meetingLink);
            pstmt.setString(9, notes);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update application status to "Interview Scheduled"
                updateApplicationStatus(applicationId, "Interview Scheduled");
                System.out.println("Interview scheduled successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error scheduling interview: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get interview details for an application
     */
    public static Interview getInterviewByApplicationId(int applicationId) {
        String sql = "SELECT * FROM interview WHERE application_id = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Interview interview = new Interview();
                interview.setInterviewId(rs.getInt("interview_id"));
                interview.setApplicationId(rs.getInt("application_id"));
                interview.setCompanyId(rs.getInt("company_id"));
                interview.setSeekerId(rs.getInt("seeker_id"));
                interview.setInterviewDate(rs.getString("interview_date"));
                interview.setInterviewTime(rs.getString("interview_time"));
                interview.setLocation(rs.getString("location"));
                interview.setType(rs.getString("type"));
                interview.setMeetingLink(rs.getString("meeting_link"));
                interview.setNotes(rs.getString("notes"));
                interview.setStatus(rs.getString("status"));
                interview.setCreatedAt(rs.getTimestamp("created_at"));
                return interview;
            }
        } catch (SQLException e) {
            System.out.println("Error getting interview: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update interview status
     */
    public static boolean updateInterviewStatus(int interviewId, String status) {
        String sql = "UPDATE interview SET status = ? WHERE interview_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, interviewId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating interview status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update application status
     */
    private static boolean updateApplicationStatus(int applicationId, String status) {
        String sql = "UPDATE application SET status = ? WHERE application_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating application status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Hire a candidate - updates application and interview status
     */
    public static boolean hireCandidate(int applicationId) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update application status to "Hired"
                String appSql = "UPDATE application SET status = 'Hired' WHERE application_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(appSql)) {
                    pstmt.setInt(1, applicationId);
                    pstmt.executeUpdate();
                }
                
                // Update interview status to "Done"
                String intSql = "UPDATE interview SET status = 'Done' WHERE application_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(intSql)) {
                    pstmt.setInt(1, applicationId);
                    pstmt.executeUpdate();
                }
                
                conn.commit();
                System.out.println("Candidate hired successfully!");
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error hiring candidate: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Reject a candidate after interview - updates application and interview status
     */
    public static boolean rejectAfterInterview(int applicationId) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update application status to "Rejected"
                String appSql = "UPDATE application SET status = 'Rejected' WHERE application_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(appSql)) {
                    pstmt.setInt(1, applicationId);
                    pstmt.executeUpdate();
                }
                
                // Update interview status to "Done"
                String intSql = "UPDATE interview SET status = 'Done' WHERE application_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(intSql)) {
                    pstmt.setInt(1, applicationId);
                    pstmt.executeUpdate();
                }
                
                conn.commit();
                System.out.println("Candidate rejected after interview.");
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error rejecting candidate: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all interviews for a company
     */
    public static List<Interview> getInterviewsByCompany(int companyId) {
        List<Interview> interviews = new ArrayList<>();
        String sql = "SELECT * FROM interview WHERE company_id = ? ORDER BY interview_date DESC, interview_time DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Interview interview = new Interview();
                interview.setInterviewId(rs.getInt("interview_id"));
                interview.setApplicationId(rs.getInt("application_id"));
                interview.setCompanyId(rs.getInt("company_id"));
                interview.setSeekerId(rs.getInt("seeker_id"));
                interview.setInterviewDate(rs.getString("interview_date"));
                interview.setInterviewTime(rs.getString("interview_time"));
                interview.setLocation(rs.getString("location"));
                interview.setType(rs.getString("type"));
                interview.setMeetingLink(rs.getString("meeting_link"));
                interview.setNotes(rs.getString("notes"));
                interview.setStatus(rs.getString("status"));
                interview.setCreatedAt(rs.getTimestamp("created_at"));
                interviews.add(interview);
            }
        } catch (SQLException e) {
            System.out.println("Error getting interviews: " + e.getMessage());
        }
        return interviews;
    }
}