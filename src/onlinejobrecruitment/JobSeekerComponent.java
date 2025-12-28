/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Component Interface for Decorator Pattern
 * Defines the base operations for job seekers
 */
public interface JobSeekerComponent {
    
    /**
     * Get the job seeker's ID
     */
    int getId();
    
    /**
     * Get the job seeker's name
     */
    String getName();
    
    /**
     * Get the job seeker's display name (with badges if premium)
     */
    String getDisplayName();
    
    /**
     * Check if the job seeker has access to premium features
     */
    boolean canAccessPremiumFeatures();
    
    /**
     * Check if the job seeker can write company reviews
     */
    boolean canWriteReviews();
    
    /**
     * Check if the job seeker can edit their profile
     */
    boolean canEditProfile();
    
    /**
     * Get the job seeker type description
     */
    String getSeekerType();
    
    /**
     * Get the underlying JobSeeker object
     */
    JobSeeker getJobSeeker();
}