package onlinejobrecruitment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

    // Constructor for Registration
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    // Constructor for Login
    public Admin(String email, String password) {
        super(null, email, password);
    }

    @Override
    public boolean register() {
        String sql = "INSERT INTO admin (name, email, password) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                System.out.println("Admin registered successfully! ID: " + this.id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Admin Registration failed: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean login() {
        String sql = "SELECT admin_id, name FROM admin WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                this.id = rs.getInt("admin_id");
                this.name = rs.getString("name");
                System.out.println("Admin Login successful! Welcome, " + name);
                return true;
            } else {
                System.out.println("Invalid Admin credentials.");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    // --- VIEW METHODS (Refactored for GUI) ---

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT company_id, name, email, phone, location, website, detail FROM company";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Company c = new Company(
                    rs.getString("name"),
                    rs.getString("email"),
                    "", // Password hidden for security
                    rs.getString("phone"),
                    rs.getString("location"),
                    rs.getString("website"),
                    rs.getString("detail")
                );
                c.id = rs.getInt("company_id"); // Manually set ID
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching companies: " + e.getMessage());
        }
        return list;
    }

    public List<JobSeeker> getAllJobSeekers() {
        List<JobSeeker> list = new ArrayList<>();
        String sql = "SELECT seeker_id, name, email, phone, address, experience, skills FROM jobseeker";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                JobSeeker s = new JobSeeker(
                    rs.getString("name"),
                    rs.getString("email"),
                    "", // Password hidden
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("experience"),
                    rs.getString("skills")
                );
                s.id = rs.getInt("seeker_id"); // Manually set ID
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching seekers: " + e.getMessage());
        }
        return list;
    }

    // --- REGULATION METHODS (DELETE) ---

    public boolean deleteCompany(int companyId) {
        String sql = "DELETE FROM company WHERE company_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, companyId);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Company and all related data deleted successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting company: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteJobSeeker(int seekerId) {
        String deleteApps = "DELETE FROM application WHERE seeker_id = ?";
        String deleteSeeker = "DELETE FROM jobseeker WHERE seeker_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try {
                // 1. Delete their applications first
                try (PreparedStatement ps1 = conn.prepareStatement(deleteApps)) {
                    ps1.setInt(1, seekerId);
                    ps1.executeUpdate();
                }

                // 2. Delete the seeker
                try (PreparedStatement ps2 = conn.prepareStatement(deleteSeeker)) {
                    ps2.setInt(1, seekerId);
                    int rows = ps2.executeUpdate();
                    
                    if (rows > 0) {
                        conn.commit();
                        System.out.println("Job Seeker deleted successfully.");
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error deleting seeker: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}