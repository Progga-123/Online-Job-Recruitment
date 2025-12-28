
package onlinejobrecruitment;


public interface JobObserver {
  
    /**
     * Called when a company posts a new job
     * @param companyName The name of the company posting the job
     * @param jobTitle The title of the newly posted job
     * @param jobId The ID of the newly posted job
     */
    void update(String companyName, String jobTitle, int jobId);
    
    /**
     * Get the ID of the observer (job seeker)
     * @return The seeker ID
     */
    int getObserverId();
}  

