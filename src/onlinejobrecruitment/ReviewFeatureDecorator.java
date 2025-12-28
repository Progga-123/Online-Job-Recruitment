/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Concrete Decorator - Adds Company Review Feature
 */
public class ReviewFeatureDecorator extends PremiumJobSeekerDecorator {
    
    public ReviewFeatureDecorator(JobSeekerComponent decoratedSeeker) {
        super(decoratedSeeker);
    }
    
    @Override
    public String getDisplayName() {
        return decoratedSeeker.getDisplayName() + " ⭐"; // Add star badge
    }
    
    @Override
    public boolean canAccessPremiumFeatures() {
        return true;
    }
    
    @Override
    public boolean canWriteReviews() {
        return true; // This decorator adds review capability
    }
    
    @Override
    public String getSeekerType() {
        return decoratedSeeker.getSeekerType() + " + Review Feature";
    }
} 