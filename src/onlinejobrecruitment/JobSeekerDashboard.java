package onlinejobrecruitment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class JobSeekerDashboard extends JFrame {
    private JobSeeker activeSeeker;
    private JobSeekerComponent seekerComponent;
    private JTable jobsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel notificationLabel;
    private JLabel premiumLabel;

    public JobSeekerDashboard(JobSeeker seeker) {
        this.activeSeeker = seeker;
        
        // Build component with decorators based on purchased features
        this.seekerComponent = PremiumManager.buildJobSeekerComponent(seeker);
        
        setTitle("Job Seeker Dashboard - " + seekerComponent.getDisplayName());
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Header with premium status
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(seekerComponent.canAccessPremiumFeatures() ? 
                                 new Color(255, 215, 0) : new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + seekerComponent.getDisplayName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        premiumLabel = new JLabel(seekerComponent.getSeekerType());
        premiumLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        premiumLabel.setForeground(seekerComponent.canAccessPremiumFeatures() ? 
                                   new Color(184, 134, 11) : Color.GRAY);
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(premiumLabel, BorderLayout.SOUTH);
        
        headerPanel.add(welcomePanel, BorderLayout.WEST);

        // Search and Notification Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search Jobs");
        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Notification section
        JPanel notifPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        notificationLabel = new JLabel("🔔 Notifications: 0");
        JButton viewNotifsButton = new JButton("View Notifications");
        notifPanel.add(notificationLabel);
        notifPanel.add(viewNotifsButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(notifPanel, BorderLayout.EAST);
        
        // Combine header and search panels
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(topPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // Center Panel - Jobs Table
        String[] columns = {"Job ID", "Title", "Company", "Location", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        jobsTable = new JTable(tableModel);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(jobsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Actions (using GridLayout for better spacing)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // First row of buttons
        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton applyButton = new JButton("Apply to Selected Job");
        JButton viewMyAppsButton = new JButton("📋 View My Applications");
        JButton subscribeButton = new JButton("Subscribe to Companies");
        JButton upgradeButton = new JButton("👑 Upgrade to Premium");
        
        viewMyAppsButton.setFont(new Font("Arial", Font.BOLD, 13));
        viewMyAppsButton.setBackground(new Color(52, 152, 219));
        viewMyAppsButton.setForeground(Color.WHITE);
        
        upgradeButton.setFont(new Font("Arial", Font.BOLD, 13));
        upgradeButton.setBackground(new Color(255, 215, 0));
        upgradeButton.setForeground(Color.BLACK);
        
        buttonRow1.add(applyButton);
        buttonRow1.add(viewMyAppsButton);
        buttonRow1.add(subscribeButton);
        buttonRow1.add(upgradeButton);
        
        // Second row of buttons
        JPanel buttonRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton writeReviewButton = new JButton("⭐ Write Review");
        JButton editProfileButton = new JButton("✏️ Edit Profile");
        JButton logoutButton = new JButton("🚪 Logout");
        
        writeReviewButton.setBackground(new Color(52, 152, 219));
        writeReviewButton.setForeground(Color.WHITE);
        writeReviewButton.setEnabled(seekerComponent.canWriteReviews());
        
        editProfileButton.setBackground(new Color(46, 204, 113));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setEnabled(seekerComponent.canEditProfile());
        
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        
        buttonRow2.add(writeReviewButton);
        buttonRow2.add(editProfileButton);
        buttonRow2.add(logoutButton);
        
        bottomPanel.add(buttonRow1);
        bottomPanel.add(buttonRow2);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        searchButton.addActionListener(e -> performSearch());
        applyButton.addActionListener(e -> showApplicationDialog());
        viewMyAppsButton.addActionListener(e -> viewMyApplications());
        subscribeButton.addActionListener(e -> showSubscriptionDialog());
        viewNotifsButton.addActionListener(e -> showNotifications());
        
        upgradeButton.addActionListener(e -> showUpgradeDialog());
        
        writeReviewButton.addActionListener(e -> {
            if (seekerComponent.canWriteReviews()) {
                new WriteReviewDialog(this, seekerComponent);
            } else {
                JOptionPane.showMessageDialog(this,
                    "This is a Premium Feature!\n\n" +
                    "Upgrade to Premium to write company reviews.",
                    "Premium Feature Required",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        editProfileButton.addActionListener(e -> {
            if (seekerComponent.canEditProfile()) {
                new EditProfileDialog(this, seekerComponent);
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this,
                    "This is a Premium Feature!\n\n" +
                    "Upgrade to Premium to edit your profile.",
                    "Premium Feature Required",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame();
                dispose();
            }
        });

        // Update notification count
        updateNotificationCount();

        setVisible(true);
    }
    
    private void showUpgradeDialog() {
        new PremiumUpgradeDialog(this, activeSeeker);
        refreshDashboard(); // Refresh to show new features if purchased
    }
    
    private void refreshDashboard() {
        // Rebuild component with updated features
        seekerComponent = PremiumManager.buildJobSeekerComponent(activeSeeker);
        
        // Update title
        setTitle("Job Seeker Dashboard - " + seekerComponent.getDisplayName());
        
        // Update premium label
        premiumLabel.setText(seekerComponent.getSeekerType());
        
        // Enable/disable buttons based on features
        Component[] components = ((JPanel)getContentPane().getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component btn : panel.getComponents()) {
                    if (btn instanceof JButton) {
                        JButton button = (JButton) btn;
                        if (button.getText().contains("Write Review")) {
                            button.setEnabled(seekerComponent.canWriteReviews());
                        } else if (button.getText().contains("Edit Profile")) {
                            button.setEnabled(seekerComponent.canEditProfile());
                        }
                    }
                }
            }
        }
    }

    private void updateNotificationCount() {
        int unreadCount = activeSeeker.getUnreadNotificationCount();
        notificationLabel.setText("🔔 Notifications: " + unreadCount);
        if (unreadCount > 0) {
            notificationLabel.setForeground(Color.RED);
            notificationLabel.setFont(notificationLabel.getFont().deriveFont(Font.BOLD));
        } else {
            notificationLabel.setForeground(Color.BLACK);
            notificationLabel.setFont(notificationLabel.getFont().deriveFont(Font.PLAIN));
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        List<Job> jobs = activeSeeker.searchJobs(keyword);

        if (jobs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No jobs found matching: " + keyword);
        } else {
            for (Job job : jobs) {
                Object[] row = {
                    job.getJobId(),
                    job.getTitle(),
                    job.getCompanyName(),
                    job.getLocation(),
                    job.getSalaryRange()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void viewMyApplications() {
        new ViewMyApplicationsDialog(this, activeSeeker);
    }

    private void showApplicationDialog() {
        int selectedRow = jobsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a job from the table first!", 
                "No Job Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jobId = (int) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String companyName = (String) tableModel.getValueAt(selectedRow, 2);

        JDialog appDialog = new JDialog(this, "Apply for Job", true);
        appDialog.setSize(600, 450);
        appDialog.setLocationRelativeTo(this);
        appDialog.setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Job Information"));
        infoPanel.add(new JLabel("  Job Title: " + jobTitle));
        infoPanel.add(new JLabel("  Company: " + companyName));
        infoPanel.add(new JLabel("  Job ID: " + jobId));
        infoPanel.add(new JLabel("  Applicant: " + activeSeeker.getName()));
        appDialog.add(infoPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel coverLetterPanel = new JPanel(new BorderLayout(5, 5));
        coverLetterPanel.add(new JLabel("Cover Letter / Message:"), BorderLayout.NORTH);
        JTextArea coverLetterArea = new JTextArea(8, 40);
        coverLetterArea.setLineWrap(true);
        coverLetterArea.setWrapStyleWord(true);
        coverLetterArea.setText("Dear Hiring Manager,\n\nI am writing to express my interest in the " + jobTitle + " position...");
        JScrollPane coverLetterScroll = new JScrollPane(coverLetterArea);
        coverLetterPanel.add(coverLetterScroll, BorderLayout.CENTER);

        JPanel resumePanel = new JPanel(new BorderLayout(10, 10));
        resumePanel.setBorder(BorderFactory.createTitledBorder("Resume / CV"));
        
        JPanel resumeInputPanel = new JPanel(new BorderLayout(5, 5));
        JTextField filePathField = new JTextField();
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse...");
        
        resumeInputPanel.add(new JLabel("Selected File: "), BorderLayout.WEST);
        resumeInputPanel.add(filePathField, BorderLayout.CENTER);
        resumeInputPanel.add(browseButton, BorderLayout.EAST);
        
        JLabel resumeInfoLabel = new JLabel("  Accepted formats: PDF, DOC, DOCX (Max 5MB)");
        resumeInfoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        resumeInfoLabel.setForeground(Color.GRAY);
        
        resumePanel.add(resumeInputPanel, BorderLayout.CENTER);
        resumePanel.add(resumeInfoLabel, BorderLayout.SOUTH);

        formPanel.add(coverLetterPanel, BorderLayout.CENTER);
        formPanel.add(resumePanel, BorderLayout.SOUTH);
        
        appDialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitButton = new JButton("Submit Application");
        JButton cancelButton = new JButton("Cancel");
        
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        submitButton.setPreferredSize(new Dimension(160, 35));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        appDialog.add(buttonPanel, BorderLayout.SOUTH);

        final File[] selectedFile = {null};
        
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Resume/CV");
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Document Files (*.pdf, *.doc, *.docx)", "pdf", "doc", "docx");
            fileChooser.setFileFilter(filter);
            
            int result = fileChooser.showOpenDialog(appDialog);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                long fileSizeInBytes = file.length();
                long maxSizeInBytes = 5 * 1024 * 1024;
                
                if (fileSizeInBytes > maxSizeInBytes) {
                    JOptionPane.showMessageDialog(appDialog, 
                        "File size exceeds 5MB limit!\nPlease select a smaller file.", 
                        "File Too Large", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                selectedFile[0] = file;
                filePathField.setText(file.getAbsolutePath());
            }
        });

        submitButton.addActionListener(e -> {
            String coverLetter = coverLetterArea.getText().trim();
            
            if (coverLetter.isEmpty()) {
                JOptionPane.showMessageDialog(appDialog, 
                    "Please write a cover letter or message!", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder resumeBuilder = new StringBuilder();
            resumeBuilder.append("=== COVER LETTER ===\n");
            resumeBuilder.append(coverLetter);
            resumeBuilder.append("\n\n=== RESUME FILE ===\n");
            
            if (selectedFile[0] != null) {
                resumeBuilder.append("File: ").append(selectedFile[0].getName()).append("\n");
                resumeBuilder.append("Path: ").append(selectedFile[0].getAbsolutePath()).append("\n");
                resumeBuilder.append("Size: ").append(selectedFile[0].length() / 1024).append(" KB");
            } else {
                resumeBuilder.append("No file attached");
            }

            String finalResume = resumeBuilder.toString();
            
            boolean success = activeSeeker.applyJob(jobId, finalResume);
            
            if (success) {
                JOptionPane.showMessageDialog(appDialog, 
                    "✅ Application Submitted Successfully!\n\n" +
                    "Job: " + jobTitle + "\n" +
                    "Company: " + companyName + "\n\n" +
                    "Good luck with your application!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                appDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(appDialog, 
                    "Failed to submit application. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> appDialog.dispose());

        appDialog.setVisible(true);
    }

    private void showSubscriptionDialog() {
        new SubscriptionDialog(this, activeSeeker);
    }

    private void showNotifications() {
        new NotificationDialog(this, activeSeeker);
        updateNotificationCount();
    }
}