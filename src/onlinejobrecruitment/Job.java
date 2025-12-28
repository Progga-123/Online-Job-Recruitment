
package onlinejobrecruitment;

public class Job {
    private int jobId;
    private String title;
    private String companyName;
    private String location;
    private String salaryRange;
    private String skills;
    private String requirement;
    
    
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
    
    @Override
    public String toString() {
        return "Job ID: " + jobId + "\nTitle: " + title + "\nCompany: " + companyName +
               "\nLocation: " + location + "\nSalary: " + salaryRange;
    }
}