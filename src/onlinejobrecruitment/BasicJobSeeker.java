/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Concrete Component - Basic Job Seeker (Regular/Free User)
 * Has limited features - no premium access
 */
public class BasicJobSeeker implements JobSeekerComponent {
    
    protected JobSeeker jobSeeker;
    
    public BasicJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }
    
    @Override
    public int getId() {
        return jobSeeker.getId();
    }
    
    @Override
    public String getName() {
        return jobSeeker.getName();
    }
    
    @Override
    public String getDisplayName() {
        return jobSeeker.getName(); // No badges for regular users
    }
    
    @Override
    public boolean canAccessPremiumFeatures() {
        return false; // Regular users don't have premium access
    }
    
    @Override
    public boolean canWriteReviews() {
        return false; // Regular users cannot write reviews
    }
    
    @Override
    public boolean canEditProfile() {
        return false; // Regular users cannot edit profile
    }
    
    @Override
    public String getSeekerType() {
        return "Regular Job Seeker";
    }
    
    @Override
    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }
}