/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

/**
 * Concrete Decorator - Adds Profile Edit Feature
 */
public class ProfileEditDecorator extends PremiumJobSeekerDecorator {
    
    public ProfileEditDecorator(JobSeekerComponent decoratedSeeker) {
        super(decoratedSeeker);
    }
    
    @Override
    public String getDisplayName() {
        return decoratedSeeker.getDisplayName() + " 👑"; // Add crown badge
    }
    
    @Override
    public boolean canAccessPremiumFeatures() {
        return true;
    }
    
    @Override
    public boolean canEditProfile() {
        return true; // This decorator adds profile edit capability
    }
    
    @Override
    public String getSeekerType() {
        return decoratedSeeker.getSeekerType() + " + Profile Edit Feature";
    }
}