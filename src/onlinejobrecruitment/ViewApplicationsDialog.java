/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog for companies to view and manage job applications
 * Updated with interview scheduling and hiring features
 */
public class ViewApplicationsDialog extends JDialog {
    private Company activeCompany;
    private JTable applicationsTable;
    private DefaultTableModel tableModel;
    private JTextArea resumeTextArea;
    
    public ViewApplicationsDialog(JFrame parent, Company company) {
        super(parent, "View Applications - " + company.getName(), true);
        this.activeCompany = company;
        
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("📋 Job Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Split Pane for table and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        
        // Top Panel - Applications Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Applications Received"));
        
        String[] columns = {"App ID", "Applicant", "Job Title", "Status", "Interview Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        applicationsTable = new JTable(tableModel);
        applicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(applicationsTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        splitPane.setTopComponent(tablePanel);
        
        // Bottom Panel - Resume Details
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Resume / Cover Letter"));
        
        resumeTextArea = new JTextArea();
        resumeTextArea.setEditable(false);
        resumeTextArea.setLineWrap(true);
        resumeTextArea.setWrapStyleWord(true);
        resumeTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resumeTextArea.setText("Select an application to view resume details...");
        
        JScrollPane resumeScroll = new JScrollPane(resumeTextArea);
        detailsPanel.add(resumeScroll, BorderLayout.CENTER);
        
        splitPane.setBottomComponent(detailsPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton viewResumeButton = new JButton("View Full Resume");
        JButton openFileButton = new JButton("📄 Open Resume File");
        JButton scheduleInterviewButton = new JButton("📅 Schedule Interview");
        JButton rejectButton = new JButton("❌ Reject");
        JButton hireButton = new JButton("✅ Hire");
        JButton rejectAfterInterviewButton = new JButton("❌ Reject After Interview");
        JButton refreshButton = new JButton("🔄 Refresh");
        JButton closeButton = new JButton("Close");
        
        scheduleInterviewButton.setBackground(new Color(52, 152, 219));
        scheduleInterviewButton.setForeground(Color.WHITE);
        rejectButton.setBackground(new Color(231, 76, 60));
        rejectButton.setForeground(Color.WHITE);
        hireButton.setBackground(new Color(46, 204, 113));
        hireButton.setForeground(Color.WHITE);
        rejectAfterInterviewButton.setBackground(new Color(192, 57, 43));
        rejectAfterInterviewButton.setForeground(Color.WHITE);
        openFileButton.setBackground(new Color(52, 152, 219));
        openFileButton.setForeground(Color.WHITE);
        
        buttonPanel.add(viewResumeButton);
        buttonPanel.add(openFileButton);
        buttonPanel.add(scheduleInterviewButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(hireButton);
        buttonPanel.add(rejectAfterInterviewButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        applicationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSelectedResume();
            }
        });
        
        viewResumeButton.addActionListener(e -> showFullResumeDialog());
        openFileButton.addActionListener(e -> openResumeFile());
        scheduleInterviewButton.addActionListener(e -> scheduleInterview());
        rejectButton.addActionListener(e -> updateStatus("Rejected"));
        hireButton.addActionListener(e -> hireCandidate());
        rejectAfterInterviewButton.addActionListener(e -> rejectAfterInterview());
        refreshButton.addActionListener(e -> loadApplications());
        closeButton.addActionListener(e -> dispose());
        
        // Load data
        loadApplications();
        
        setVisible(true);
    }
    
    private void loadApplications() {
        tableModel.setRowCount(0);
        resumeTextArea.setText("Loading applications...");
        
        List<Resume> applications = activeCompany.viewApplications();
        
        if (applications.isEmpty()) {
            resumeTextArea.setText("No applications received yet.");
            JOptionPane.showMessageDialog(this,
                "No applications have been received for your job postings.",
                "No Applications",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Resume app : applications) {
            // Get interview status
            Interview interview = InterviewManager.getInterviewByApplicationId(app.getApplicationId());
            String interviewStatus = interview != null ? interview.getStatus() : "Not Scheduled";
            
            Object[] row = {
                app.getApplicationId(),
                app.getSeekerName(),
                app.getJobTitle(),
                app.getStatus(),
                interviewStatus,
                app.getApplicationDate()
            };
            tableModel.addRow(row);
        }
        
        resumeTextArea.setText("Select an application to view resume details...\n\n" +
                              "Total Applications: " + applications.size());
    }
    
    private void viewSelectedResume() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String resumeContent = activeCompany.getResumeContent(applicationId);
        
        if (resumeContent != null && !resumeContent.startsWith("❌")) {
            resumeTextArea.setText(resumeContent);
            resumeTextArea.setCaretPosition(0);
        } else {
            resumeTextArea.setText(resumeContent != null ? resumeContent : 
                "Error loading resume. You may not have permission to view this application.");
        }
    }
    
    private void showFullResumeDialog() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        Resume resume = activeCompany.viewResume(applicationId);
        
        if (resume == null) {
            JOptionPane.showMessageDialog(this,
                "Access Denied! You cannot view this resume.",
                "Authorization Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog resumeDialog = new JDialog(this, "Full Resume - " + resume.getSeekerName(), true);
        resumeDialog.setSize(700, 600);
        resumeDialog.setLocationRelativeTo(this);
        resumeDialog.setLayout(new BorderLayout());
        
        JTextArea fullResumeArea = new JTextArea(resume.getResumeContent());
        fullResumeArea.setEditable(false);
        fullResumeArea.setLineWrap(true);
        fullResumeArea.setWrapStyleWord(true);
        fullResumeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scroll = new JScrollPane(fullResumeArea);
        resumeDialog.add(scroll, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> resumeDialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        resumeDialog.add(btnPanel, BorderLayout.SOUTH);
        
        resumeDialog.setVisible(true);
    }
    
    private void scheduleInterview() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String applicantName = (String) tableModel.getValueAt(selectedRow, 1);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 2);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        
        // Check if already hired or rejected
        if (currentStatus.equals("Hired") || currentStatus.equals("Rejected")) {
            JOptionPane.showMessageDialog(this,
                "Cannot schedule interview for " + currentStatus.toLowerCase() + " applications.",
                "Invalid Action",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get Resume to get seeker ID
        Resume resume = activeCompany.viewResume(applicationId);
        if (resume == null) {
            JOptionPane.showMessageDialog(this,
                "Error loading application details.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Open schedule dialog
        new ScheduleInterviewDialog(this, applicationId, activeCompany.getId(), 
                                    resume.getSeekerId(), applicantName, jobTitle);
        loadApplications(); // Refresh
    }
    
    private void updateStatus(String newStatus) {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String applicantName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to " + newStatus.toLowerCase() + " " + applicantName + "'s application?",
            "Confirm Status Change",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = activeCompany.updateApplicationStatus(applicationId, newStatus);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Application status updated to: " + newStatus,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadApplications();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update status. You may not have permission.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hireCandidate() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String applicantName = (String) tableModel.getValueAt(selectedRow, 1);
        String interviewStatus = (String) tableModel.getValueAt(selectedRow, 4);
        
        // Check if interview was scheduled
        if (!interviewStatus.equals("Scheduled")) {
            int proceed = JOptionPane.showConfirmDialog(this,
                "No interview was scheduled for this candidate.\n" +
                "Do you still want to hire them?",
                "No Interview",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (proceed != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to hire " + applicantName + "?",
            "Confirm Hiring",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = InterviewManager.hireCandidate(applicationId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "✅ " + applicantName + " has been hired!\n\n" +
                    "Application status: Hired\n" +
                    "Interview status: Done",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadApplications();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to hire candidate. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void rejectAfterInterview() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String applicantName = (String) tableModel.getValueAt(selectedRow, 1);
        String interviewStatus = (String) tableModel.getValueAt(selectedRow, 4);
        
        if (!interviewStatus.equals("Scheduled")) {
            JOptionPane.showMessageDialog(this,
                "No interview was scheduled for this application.",
                "No Interview",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reject " + applicantName + " after interview?",
            "Confirm Rejection",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = InterviewManager.rejectAfterInterview(applicationId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    applicantName + " has been rejected after interview.\n\n" +
                    "Application status: Rejected\n" +
                    "Interview status: Done",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadApplications();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to reject candidate. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openResumeFile() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        String resumeContent = activeCompany.getResumeContent(applicationId);
        
        if (resumeContent == null || resumeContent.startsWith("❌")) {
            JOptionPane.showMessageDialog(this,
                "Access Denied or Resume Not Found!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String filePath = extractFilePath(resumeContent);
        
        if (filePath == null || filePath.equals("No file attached")) {
            JOptionPane.showMessageDialog(this,
                "No resume file was attached to this application.\nOnly cover letter is available.",
                "No File",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        java.io.File resumeFile = new java.io.File(filePath);
        
        if (!resumeFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Resume file not found at:\n" + filePath + "\n\n" +
                "The file may have been moved or deleted.\n" +
                "Would you like to copy the file path to clipboard?",
                "File Not Found",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                copyToClipboard(filePath);
                JOptionPane.showMessageDialog(this,
                    "File path copied to clipboard!",
                    "Copied",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }
        
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                    desktop.open(resumeFile);
                    System.out.println("[GUI] Opened resume file: " + filePath);
                } else {
                    showFileLocationDialog(filePath);
                }
            } else {
                showFileLocationDialog(filePath);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Could not open file automatically.\n" +
                "File location: " + filePath + "\n\n" +
                "Error: " + ex.getMessage(),
                "Cannot Open File",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String extractFilePath(String resumeContent) {
        String[] lines = resumeContent.split("\n");
        for (String line : lines) {
            if (line.startsWith("Path: ")) {
                return line.substring(6).trim();
            }
        }
        return null;
    }
    
    private void showFileLocationDialog(String filePath) {
        JDialog locationDialog = new JDialog(this, "Resume File Location", true);
        locationDialog.setSize(600, 200);
        locationDialog.setLocationRelativeTo(this);
        locationDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel msgLabel = new JLabel("<html>Cannot open file automatically.<br>" +
                                     "Please navigate to this location manually:</html>");
        panel.add(msgLabel, BorderLayout.NORTH);
        
        JTextField pathField = new JTextField(filePath);
        pathField.setEditable(false);
        pathField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(pathField, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton copyBtn = new JButton("Copy Path");
        JButton closeBtn = new JButton("Close");
        
        copyBtn.addActionListener(e -> {
            copyToClipboard(filePath);
            JOptionPane.showMessageDialog(locationDialog, "Path copied to clipboard!");
        });
        
        closeBtn.addActionListener(e -> locationDialog.dispose());
        
        btnPanel.add(copyBtn);
        btnPanel.add(closeBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        locationDialog.add(panel);
        locationDialog.setVisible(true);
    }
    
    private void copyToClipboard(String text) {
        java.awt.datatransfer.StringSelection selection = 
            new java.awt.datatransfer.StringSelection(text);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }
}