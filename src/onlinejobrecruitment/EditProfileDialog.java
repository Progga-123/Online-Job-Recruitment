/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Dialog for premium users to edit their profile
 */
public class EditProfileDialog extends JDialog {
    private JobSeekerComponent seekerComponent;
    
    public EditProfileDialog(JFrame parent, JobSeekerComponent seekerComponent) {
        super(parent, "Edit Profile - Premium Feature", true);
        this.seekerComponent = seekerComponent;
        
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 215, 0)); // Gold
        JLabel titleLabel = new JLabel("👑 Edit Your Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Load current profile data
        JobSeeker seeker = seekerComponent.getJobSeeker();
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Name
        formPanel.add(new JLabel("Full Name: *"));
        JTextField nameField = new JTextField(seeker.getName());
        formPanel.add(nameField);
        
        // Email (Read-only)
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(seeker.getEmail());
        emailField.setEditable(false);
        emailField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(emailField);
        
        // Phone
        formPanel.add(new JLabel("Phone: *"));
        JTextField phoneField = new JTextField(seeker.getPhone());
        formPanel.add(phoneField);
        
        // Address
        formPanel.add(new JLabel("Address: *"));
        JTextField addressField = new JTextField(seeker.getAddress());
        formPanel.add(addressField);
        
        // Experience
        formPanel.add(new JLabel("Experience:"));
        JTextField experienceField = new JTextField(seeker.getExperience());
        formPanel.add(experienceField);
        
        // Skills Label
        formPanel.add(new JLabel("Skills:"));
        formPanel.add(new JLabel(""));
        
        add(formPanel, BorderLayout.CENTER);
        
        // Skills Text Area
        JPanel skillsPanel = new JPanel(new BorderLayout());
        skillsPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        
        JTextArea skillsArea = new JTextArea(seeker.getSkills(), 4, 40);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);
        skillsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane skillsScroll = new JScrollPane(skillsArea);
        skillsScroll.setBorder(BorderFactory.createTitledBorder("Skills"));
        skillsPanel.add(skillsScroll, BorderLayout.CENTER);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(skillsPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton saveButton = new JButton("💾 Save Changes");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(150, 35));
        
        cancelButton.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String experience = experienceField.getText().trim();
            String skills = skillsArea.getText().trim();
            
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = updateProfile(seeker.getId(), name, phone, address, experience, skills);
            
            if (success) {
                // Update the seeker object
                seeker.name = name;
                
                JOptionPane.showMessageDialog(this,
                    "✅ Profile Updated Successfully!\n\n" +
                    "Your profile changes have been saved.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update profile. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        setVisible(true);
    }
    
    private boolean updateProfile(int seekerId, String name, String phone, 
                                 String address, String experience, String skills) {
        String sql = "UPDATE jobseeker SET name = ?, phone = ?, address = ?, " +
                     "experience = ?, skills = ? WHERE seeker_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setString(4, experience);
            pstmt.setString(5, skills);
            pstmt.setInt(6, seekerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
        return false;
    }
}