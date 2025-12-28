/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import java.util.List;

/**
 * Subject Interface for Proxy Pattern
 * Defines operations for accessing resume/application data
 */
public interface ResumeAccess {
    
    /**
     * Get all applications for a specific company
     * @param companyId The company requesting applications
     * @return List of Resume objects
     */
    List<Resume> getApplicationsByCompany(int companyId);
    
    /**
     * Get a specific resume by application ID
     * @param applicationId The application ID
     * @param requestingCompanyId The company requesting access
     * @return Resume object or null if access denied
     */
    Resume getResumeByApplicationId(int applicationId, int requestingCompanyId);
    
    /**
     * Get resume content (the actual file/text)
     * @param applicationId The application ID
     * @param requestingCompanyId The company requesting access
     * @return Resume content string or access denied message
     */
    String getResumeContent(int applicationId, int requestingCompanyId);
    
    /**
     * Update application status
     * @param applicationId The application to update
     * @param newStatus The new status (Pending, Accepted, Rejected)
     * @param companyId The company making the update
     * @return true if successful
     */
    boolean updateApplicationStatus(int applicationId, String newStatus, int companyId);
}
