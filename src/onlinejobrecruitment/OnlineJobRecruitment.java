/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package onlinejobrecruitment;

import java.util.Scanner;
import java.sql.Date;

public class OnlineJobRecruitment {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Online Job Recruitment System ===\n");

        while (true) {
            System.out.println("\n1. Company Registration");
            System.out.println("2. Company Login");
            System.out.println("3. Job Seeker Registration");
            System.out.println("4. Job Seeker Login");
            System.out.println("5. Admin Login");
            System.out.println("6. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    companyRegistration();
                    break;
                case 2:
                    companyLogin();
                    break;
                case 3:
                    jobSeekerRegistration();
                    break;
                case 4:
                    jobSeekerLogin();
                    break;
                case 5:
                    adminLogin(); 
                    break;
                case 6:
                    System.out.println("Thank you for using Job Recruitment System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void companyRegistration() {
        System.out.println("\n=== Company Registration ===");

        System.out.print("Company Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Website: ");
        String website = scanner.nextLine();

        System.out.print("Company Details: ");
        String detail = scanner.nextLine();

        Company company = new Company(name, email, password, phone, location, website, detail);
        company.register();
    }

    private static void companyLogin() {
        System.out.println("\n=== Company Login ===");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Company company = new Company(email, password);

        if (company.login()) {
            companyMenu(company);
        }
    }

    private static void companyMenu(Company company) {
        while (true) {
            System.out.println("\n=== Company Menu ===");
            System.out.println("1. Post Job");
            System.out.println("2. View Applications");
            System.out.println("3. Logout");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    postJob(company);
                    break;
                case 2:
                    System.out.println("View Applications - Feature coming soon!");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void postJob(Company company) {
        System.out.println("\n=== Post New Job ===");

        System.out.print("Job Title: ");
        String title = scanner.nextLine();

        System.out.print("Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Required Skills: ");
        String skills = scanner.nextLine();

        System.out.print("Experience Required: ");
        String experience = scanner.nextLine();

        System.out.print("Salary Range: ");
        String salaryRange = scanner.nextLine();

        System.out.print("Deadline (YYYY-MM-DD): ");
        String deadlineStr = scanner.nextLine();
        Date deadline = Date.valueOf(deadlineStr);

        System.out.print("Number of Vacancies: ");
        int vacancy = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Platform (Remote/Onsite/Hybrid): ");
        String platform = scanner.nextLine();

        System.out.print("Requirements: ");
        String requirement = scanner.nextLine();

        company.postJob(title, categoryId, skills, experience, salaryRange,
                deadline, vacancy, location, platform, requirement);
    }

    private static void jobSeekerRegistration() {
        System.out.println("\n=== Job Seeker Registration ===");

        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Experience: ");
        String experience = scanner.nextLine();

        System.out.print("Skills: ");
        String skills = scanner.nextLine();

        JobSeeker seeker = new JobSeeker(name, email, password, phone, address, experience, skills);
        seeker.register();
    }

    private static void jobSeekerLogin() {
        System.out.println("\n=== Job Seeker Login ===");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        JobSeeker seeker = new JobSeeker(email, password);

        if (seeker.login()) {
            jobSeekerMenu(seeker);
        }
    }

    private static void jobSeekerMenu(JobSeeker seeker) {
        while (true) {
            System.out.println("\n=== Job Seeker Menu ===");
            System.out.println("1. Search Jobs");
            System.out.println("2. Apply for Job");
            System.out.println("3. View My Applications");
            System.out.println("4. Logout");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchJobs(seeker);
                    break;
                case 2:
                    applyForJob(seeker);
                    break;
                case 3:
                    System.out.println("View Applications - Feature coming soon!");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void searchJobs(JobSeeker seeker) {
        System.out.print("\nEnter search keyword: ");
        String keyword = scanner.nextLine();

        var jobs = seeker.searchJobs(keyword);

        if (jobs.isEmpty()) {
            System.out.println("No jobs found.");
        } else {
            System.out.println("\n=== Search Results ===");
            for (Job job : jobs) {
                System.out.println("\n" + job.toString());
                System.out.println("---");
            }
        }
    }

    private static void applyForJob(JobSeeker seeker) {
        System.out.print("\nEnter Job ID: ");
        int jobId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Resume/Cover Letter: ");
        String resume = scanner.nextLine();

        seeker.applyJob(jobId, resume);
    }
private static void adminLogin() {
    System.out.println("\n=== Admin Login ===");
    System.out.print("Email: ");
    String email = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    Admin admin = new Admin(email, password);
    if (admin.login()) {
        adminMenu(admin);
    }
}

private static void adminMenu(Admin admin) {
    while (true) {
        System.out.println("\n=== Admin Dashboard ===");
        System.out.println("1. View All Companies");
        System.out.println("2. View All Job Seekers");
        System.out.println("3. Delete Company (Regulate)");
        System.out.println("4. Delete Job Seeker (Regulate)");
        System.out.println("5. Create New Admin"); // Helpful for creating more admins
        System.out.println("6. Logout");
        System.out.print("Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: admin.getAllCompanies(); break;
            case 2: admin.getAllJobSeekers(); break;
            case 3:
                System.out.print("Enter Company ID to delete: ");
                int cid = scanner.nextInt();
                scanner.nextLine();
                admin.deleteCompany(cid);
                break;
            case 4:
                System.out.print("Enter Seeker ID to delete: ");
                int sid = scanner.nextInt();
                scanner.nextLine();
                admin.deleteJobSeeker(sid);
                break;
            case 5:
                System.out.print("New Admin Name: ");
                String name = scanner.nextLine();
                System.out.print("New Admin Email: ");
                String mail = scanner.nextLine();
                System.out.print("New Admin Password: ");
                String pass = scanner.nextLine();
                new Admin(name, mail, pass).register();
                break;
            case 6: return;
            default: System.out.println("Invalid option.");
        }
    }
}
}