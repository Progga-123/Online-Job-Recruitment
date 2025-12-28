/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Represents a resume/application document
 */
public class Resume {
    private int applicationId;
    private int seekerId;
    private String seekerName;
    private int jobId;
    private String jobTitle;
    private int companyId;
    private String resumeContent;
    private String status;
    private String applicationDate;
    
    public Resume() {}
    
    public Resume(int applicationId, int seekerId, String seekerName, int jobId, 
                  String jobTitle, int companyId, String resumeContent, 
                  String status, String applicationDate) {
        this.applicationId = applicationId;
        this.seekerId = seekerId;
        this.seekerName = seekerName;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyId = companyId;
        this.resumeContent = resumeContent;
        this.status = status;
        this.applicationDate = applicationDate;
    }
    
    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    
    public String getSeekerName() { return seekerName; }
    public void setSeekerName(String seekerName) { this.seekerName = seekerName; }
    
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    
    public String getResumeContent() { return resumeContent; }
    public void setResumeContent(String resumeContent) { this.resumeContent = resumeContent; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }
    
    @Override
    public String toString() {
        return "Application ID: " + applicationId + 
               "\nApplicant: " + seekerName + 
               "\nJob: " + jobTitle + 
               "\nStatus: " + status + 
               "\nDate: " + applicationDate;
    }
}