package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class CompanyDashboard extends JFrame {
    private Company activeCompany;
    private JLabel subscriberLabel;
    private JLabel reviewLabel;

    public CompanyDashboard(Company company) {
        this.activeCompany = company;
        setTitle("Company Dashboard - " + company.getName());
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Company Info
        JPanel topPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + company.getName() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        subscriberLabel = new JLabel("", JLabel.CENTER);
        subscriberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        updateSubscriberCount();
        
        reviewLabel = new JLabel("", JLabel.CENTER);
        reviewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        updateReviewStats();
        
        JLabel infoLabel = new JLabel("Manage your job postings and view applications", JLabel.CENTER);
        
        topPanel.add(welcomeLabel);
        topPanel.add(subscriberLabel);
        topPanel.add(reviewLabel);
        topPanel.add(infoLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Action Buttons
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton postJobBtn = new JButton("📝 Post New Job");
        JButton viewAppsBtn = new JButton("📋 View Applications");
        JButton viewReviewsBtn = new JButton("⭐ View Company Reviews");
        JButton logoutBtn = new JButton("🚪 Logout");
        
        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        postJobBtn.setFont(buttonFont);
        viewAppsBtn.setFont(buttonFont);
        viewReviewsBtn.setFont(buttonFont);
        logoutBtn.setFont(buttonFont);
        
        viewReviewsBtn.setBackground(new Color(255, 215, 0));
        
        centerPanel.add(postJobBtn);
        centerPanel.add(viewAppsBtn);
        centerPanel.add(viewReviewsBtn);
        centerPanel.add(logoutBtn);
        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        postJobBtn.addActionListener(e -> {
            showPostJobDialog();
            updateSubscriberCount(); // Update after posting
        });

        viewAppsBtn.addActionListener(e -> {
            new ViewApplicationsDialog(this, activeCompany);
        });
        
        viewReviewsBtn.addActionListener(e -> {
            new ViewCompanyReviewsDialog(this, activeCompany);
            updateReviewStats(); // Update after viewing
        });

        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void updateSubscriberCount() {
        int count = activeCompany.getSubscriberCount();
        subscriberLabel.setText("👥 Subscribers: " + count + " job seekers are following your company");
        if (count > 0) {
            subscriberLabel.setForeground(new Color(0, 128, 0)); // Green
        }
    }
    
    private void updateReviewStats() {
        int reviewCount = ReviewManager.getReviewCount(activeCompany.getId());
        double avgRating = ReviewManager.getAverageRating(activeCompany.getId());
        
        if (reviewCount > 0) {
            reviewLabel.setText(String.format("⭐ Rating: %.1f / 5.0 (%d reviews)", avgRating, reviewCount));
            reviewLabel.setForeground(new Color(255, 140, 0)); // Orange
        } else {
            reviewLabel.setText("⭐ No reviews yet");
            reviewLabel.setForeground(Color.GRAY);
        }
    }

    private void showPostJobDialog() {
        JDialog dialog = new JDialog(this, "Post New Job", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        mainPanel.add(new JLabel("Job Title: *"));
        JTextField titleField = new JTextField();
        mainPanel.add(titleField);
        
        mainPanel.add(new JLabel("Category ID: *"));
        JTextField categoryField = new JTextField("1");
        mainPanel.add(categoryField);
        
        mainPanel.add(new JLabel("Required Skills: *"));
        JTextField skillsField = new JTextField();
        mainPanel.add(skillsField);
        
        mainPanel.add(new JLabel("Experience Required:"));
        JTextField experienceField = new JTextField("0-2 years");
        mainPanel.add(experienceField);
        
        mainPanel.add(new JLabel("Salary Range: *"));
        JTextField salaryField = new JTextField();
        mainPanel.add(salaryField);
        
        mainPanel.add(new JLabel("Deadline (YYYY-MM-DD): *"));
        JTextField deadlineField = new JTextField("2025-12-31");
        mainPanel.add(deadlineField);
        
        mainPanel.add(new JLabel("Number of Vacancies: *"));
        JTextField vacancyField = new JTextField("1");
        mainPanel.add(vacancyField);
        
        mainPanel.add(new JLabel("Location: *"));
        JTextField locationField = new JTextField();
        mainPanel.add(locationField);
        
        mainPanel.add(new JLabel("Platform: *"));
        JComboBox<String> platformCombo = new JComboBox<>(new String[]{"Remote", "Onsite", "Hybrid"});
        mainPanel.add(platformCombo);
        
        mainPanel.add(new JLabel("Requirements:"));
        JTextArea requirementArea = new JTextArea(3, 20);
        requirementArea.setLineWrap(true);
        requirementArea.setWrapStyleWord(true);
        JScrollPane requirementScroll = new JScrollPane(requirementArea);
        mainPanel.add(requirementScroll);
        
        JLabel noteLabel = new JLabel("* Required fields", JLabel.LEFT);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        noteLabel.setForeground(Color.GRAY);
        mainPanel.add(noteLabel);
        mainPanel.add(new JLabel());
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton postButton = new JButton("Post Job");
        JButton cancelButton = new JButton("Cancel");
        
        postButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        postButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(postButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        postButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String skills = skillsField.getText().trim();
                String salary = salaryField.getText().trim();
                String location = locationField.getText().trim();
                
                if (title.isEmpty() || skills.isEmpty() || salary.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int categoryId = Integer.parseInt(categoryField.getText().trim());
                String experience = experienceField.getText().trim();
                Date deadline = Date.valueOf(deadlineField.getText().trim());
                int vacancy = Integer.parseInt(vacancyField.getText().trim());
                String platform = (String) platformCombo.getSelectedItem();
                String requirement = requirementArea.getText().trim();
                
                boolean success = activeCompany.postJob(
                    title, categoryId, skills, experience, salary, 
                    deadline, vacancy, location, platform, requirement
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ Job Posted Successfully!\n\n" + 
                        "Job Title: " + title + "\n" +
                        "Location: " + location + "\n" +
                        "Platform: " + platform + "\n\n" +
                        "All " + activeCompany.getSubscriberCount() + " subscribers have been notified!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateSubscriberCount();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to post job. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
}