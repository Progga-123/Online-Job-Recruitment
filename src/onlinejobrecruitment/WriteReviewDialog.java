/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for premium users to write company reviews
 */
public class WriteReviewDialog extends JDialog {
    private JobSeekerComponent seekerComponent;
    
    public WriteReviewDialog(JFrame parent, JobSeekerComponent seekerComponent) {
        super(parent, "Write Company Review", true);
        this.seekerComponent = seekerComponent;
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("⭐ Write Company Review");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        
        // Company Selection
        fieldsPanel.add(new JLabel("Select Company: *"));
        JComboBox<CompanyItem> companyCombo = new JComboBox<>();
        loadCompanies(companyCombo);
        fieldsPanel.add(companyCombo);
        
        // Rating
        fieldsPanel.add(new JLabel("Rating (1-5 stars): *"));
        JComboBox<String> ratingCombo = new JComboBox<>(new String[]{"5 ⭐⭐⭐⭐⭐", "4 ⭐⭐⭐⭐", "3 ⭐⭐⭐", "2 ⭐⭐", "1 ⭐"});
        fieldsPanel.add(ratingCombo);
        
        // Review Text Label
        fieldsPanel.add(new JLabel("Your Review: *"));
        fieldsPanel.add(new JLabel(""));
        
        formPanel.add(fieldsPanel, BorderLayout.NORTH);
        
        // Review Text Area
        JTextArea reviewArea = new JTextArea(10, 40);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        reviewArea.setFont(new Font("Arial", Font.PLAIN, 13));
        reviewArea.setText("Share your experience working with or interviewing at this company...");
        
        JScrollPane reviewScroll = new JScrollPane(reviewArea);
        reviewScroll.setBorder(BorderFactory.createTitledBorder("Review Text"));
        formPanel.add(reviewScroll, BorderLayout.CENTER);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton submitButton = new JButton("✅ Submit Review");
        JButton cancelButton = new JButton("Cancel");
        
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setPreferredSize(new Dimension(150, 35));
        
        cancelButton.setPreferredSize(new Dimension(100, 35));
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        submitButton.addActionListener(e -> {
            CompanyItem selected = (CompanyItem) companyCombo.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a company!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int rating = 5 - ratingCombo.getSelectedIndex(); // Convert index to rating
            String reviewText = reviewArea.getText().trim();
            
            if (reviewText.isEmpty() || reviewText.startsWith("Share your experience")) {
                JOptionPane.showMessageDialog(this,
                    "Please write your review!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = ReviewManager.addReview(
                seekerComponent.getId(),
                selected.id,
                rating,
                reviewText
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "✅ Review Submitted Successfully!\n\n" +
                    "Company: " + selected.name + "\n" +
                    "Rating: " + rating + " stars\n\n" +
                    "Thank you for your feedback!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to submit review. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        setVisible(true);
    }
    
    private void loadCompanies(JComboBox<CompanyItem> combo) {
        String sql = "SELECT company_id, name FROM company ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                combo.addItem(new CompanyItem(rs.getInt("company_id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading companies: " + e.getMessage());
        }
    }
    
    // Helper class for combo box items
    private static class CompanyItem {
        int id;
        String name;
        
        CompanyItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}