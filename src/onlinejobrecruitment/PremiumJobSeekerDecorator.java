/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Abstract Decorator for Premium Features
 */
public abstract class PremiumJobSeekerDecorator implements JobSeekerComponent {
    
    protected JobSeekerComponent decoratedSeeker;
    
    public PremiumJobSeekerDecorator(JobSeekerComponent decoratedSeeker) {
        this.decoratedSeeker = decoratedSeeker;
    }
    
    @Override
    public int getId() {
        return decoratedSeeker.getId();
    }
    
    @Override
    public String getName() {
        return decoratedSeeker.getName();
    }
    
    @Override
    public String getDisplayName() {
        return decoratedSeeker.getDisplayName();
    }
    
    @Override
    public boolean canAccessPremiumFeatures() {
        return decoratedSeeker.canAccessPremiumFeatures();
    }
    
    @Override
    public boolean canWriteReviews() {
        return decoratedSeeker.canWriteReviews();
    }
    
    @Override
    public boolean canEditProfile() {
        return decoratedSeeker.canEditProfile();
    }
    
    @Override
    public String getSeekerType() {
        return decoratedSeeker.getSeekerType();
    }
    
    @Override
    public JobSeeker getJobSeeker() {
        return decoratedSeeker.getJobSeeker();
    }
}