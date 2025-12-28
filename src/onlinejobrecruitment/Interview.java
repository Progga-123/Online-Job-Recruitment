/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;
import java.sql.Timestamp;
/**
 * Represents an interview scheduled for a job application
 */
public class Interview {
    private int interviewId;
    private int applicationId;
    private int companyId;
    private int seekerId;
    private String interviewDate;
    private String interviewTime;
    private String location;
    private String type; // "In-person", "Virtual", "Phone"
    private String meetingLink;
    private String notes;
    private String status; // "Scheduled", "Completed", "Cancelled", "Done"
    private Timestamp createdAt;
    
    public Interview() {}
    
    public Interview(int applicationId, int companyId, int seekerId, 
                    String interviewDate, String interviewTime, String location, 
                    String type, String meetingLink, String notes) {
        this.applicationId = applicationId;
        this.companyId = companyId;
        this.seekerId = seekerId;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.location = location;
        this.type = type;
        this.meetingLink = meetingLink;
        this.notes = notes;
        this.status = "Scheduled";
    }
    
    // Getters and Setters
    public int getInterviewId() { return interviewId; }
    public void setInterviewId(int interviewId) { this.interviewId = interviewId; }
    
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    
    public String getInterviewDate() { return interviewDate; }
    public void setInterviewDate(String interviewDate) { this.interviewDate = interviewDate; }
    
    public String getInterviewTime() { return interviewTime; }
    public void setInterviewTime(String interviewTime) { this.interviewTime = interviewTime; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Interview ID: " + interviewId + 
               "\nDate: " + interviewDate + " at " + interviewTime +
               "\nType: " + type + 
               "\nLocation: " + location +
               "\nStatus: " + status;
    }
}