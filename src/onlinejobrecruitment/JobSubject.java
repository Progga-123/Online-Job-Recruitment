/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package onlinejobrecruitment;


/**
 * Subject interface for the Observer Design Pattern
 * Companies implement this to manage their subscribers
 */
public interface JobSubject {
    /**
     * Add a job seeker as a subscriber
     * @param observer The job seeker subscribing
     */
    void attach(JobObserver observer);
    
    /**
     * Remove a job seeker from subscribers
     * @param observer The job seeker unsubscribing
     */
    void detach(JobObserver observer);
    
    /**
     * Notify all subscribers about a new job posting
     * @param jobTitle The title of the new job
     * @param jobId The ID of the new job
     */
    void notifyObservers(String jobTitle, int jobId);
}