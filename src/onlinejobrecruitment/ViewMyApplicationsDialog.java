/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for job seekers to view their application status and interview details
 */
public class ViewMyApplicationsDialog extends JDialog {
    private JobSeeker activeSeeker;
    private JTable applicationsTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    
    public ViewMyApplicationsDialog(JFrame parent, JobSeeker seeker) {
        super(parent, "My Applications - " + seeker.getName(), true);
        this.activeSeeker = seeker;
        
        setSize(1000, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("📋 My Job Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        
        // Top Panel - Applications Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("My Applications"));
        
        String[] columns = {"App ID", "Job Title", "Company", "Status", "Interview Status", "Applied Date"};
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
        
        // Bottom Panel - Details
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Application & Interview Details"));
        
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setText("Select an application to view details...");
        
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);
        
        splitPane.setBottomComponent(detailsPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton viewDetailsButton = new JButton("View Full Details");
        JButton refreshButton = new JButton("🔄 Refresh");
        JButton closeButton = new JButton("Close");
        
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 13));
        viewDetailsButton.setBackground(new Color(52, 152, 219));
        viewDetailsButton.setForeground(Color.WHITE);
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        applicationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSelectedApplication();
            }
        });
        
        viewDetailsButton.addActionListener(e -> showFullDetailsDialog());
        refreshButton.addActionListener(e -> loadApplications());
        closeButton.addActionListener(e -> dispose());
        
        // Load data
        loadApplications();
        
        setVisible(true);
    }
    
    private void loadApplications() {
        tableModel.setRowCount(0);
        detailsArea.setText("Loading applications...");
        
        List<ApplicationDetails> applications = getMyApplications();
        
        if (applications.isEmpty()) {
            detailsArea.setText("You haven't applied to any jobs yet.\n\n" +
                              "Search for jobs and apply to start receiving updates!");
            JOptionPane.showMessageDialog(this,
                "You haven't applied to any jobs yet.",
                "No Applications",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (ApplicationDetails app : applications) {
            Object[] row = {
                app.applicationId,
                app.jobTitle,
                app.companyName,
                app.status,
                app.interviewStatus,
                app.applicationDate
            };
            tableModel.addRow(row);
        }
        
        detailsArea.setText("Select an application to view details...\n\n" +
                          "Total Applications: " + applications.size());
    }
    
    private void viewSelectedApplication() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        ApplicationDetails details = getApplicationDetails(applicationId);
        
        if (details != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════\n");
            sb.append("           APPLICATION DETAILS\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            
            sb.append("📋 Application ID: ").append(details.applicationId).append("\n");
            sb.append("💼 Job Title: ").append(details.jobTitle).append("\n");
            sb.append("🏢 Company: ").append(details.companyName).append("\n");
            sb.append("📍 Location: ").append(details.location).append("\n");
            sb.append("💰 Salary: ").append(details.salary).append("\n");
            sb.append("📅 Applied Date: ").append(details.applicationDate).append("\n");
            sb.append("📊 Status: ").append(details.status).append("\n\n");
            
            if (details.hasInterview) {
                sb.append("═══════════════════════════════════════════════════\n");
                sb.append("           INTERVIEW DETAILS\n");
                sb.append("═══════════════════════════════════════════════════\n\n");
                
                sb.append("📅 Interview Date: ").append(details.interviewDate).append("\n");
                sb.append("⏰ Interview Time: ").append(details.interviewTime).append("\n");
                sb.append("📍 Location: ").append(details.interviewLocation).append("\n");
                sb.append("💻 Type: ").append(details.interviewType).append("\n");
                
                if (details.meetingLink != null && !details.meetingLink.isEmpty()) {
                    sb.append("🔗 Meeting Link: ").append(details.meetingLink).append("\n");
                }
                
                if (details.interviewNotes != null && !details.interviewNotes.isEmpty()) {
                    sb.append("\n📝 Interview Notes:\n");
                    sb.append(details.interviewNotes).append("\n");
                }
                
                sb.append("\n📊 Interview Status: ").append(details.interviewStatus).append("\n");
            } else {
                sb.append("\n⏳ No interview scheduled yet.\n");
                if (details.status.equals("Pending")) {
                    sb.append("Your application is under review.\n");
                }
            }
            
            detailsArea.setText(sb.toString());
            detailsArea.setCaretPosition(0);
        }
    }
    
    private void showFullDetailsDialog() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an application first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int applicationId = (int) tableModel.getValueAt(selectedRow, 0);
        ApplicationDetails details = getApplicationDetails(applicationId);
        
        if (details == null) {
            JOptionPane.showMessageDialog(this,
                "Error loading application details.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create full details dialog
        JDialog detailsDialog = new JDialog(this, "Application Details - " + details.jobTitle, true);
        detailsDialog.setSize(700, 600);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout(10, 10));
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Application Info
        JPanel appPanel = new JPanel(new BorderLayout());
        JTextArea appArea = new JTextArea();
        appArea.setEditable(false);
        appArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder appInfo = new StringBuilder();
        appInfo.append("APPLICATION INFORMATION\n\n");
        appInfo.append("Application ID: ").append(details.applicationId).append("\n");
        appInfo.append("Job Title: ").append(details.jobTitle).append("\n");
        appInfo.append("Company: ").append(details.companyName).append("\n");
        appInfo.append("Location: ").append(details.location).append("\n");
        appInfo.append("Salary Range: ").append(details.salary).append("\n");
        appInfo.append("Platform: ").append(details.platform).append("\n");
        appInfo.append("Applied Date: ").append(details.applicationDate).append("\n");
        appInfo.append("Status: ").append(details.status).append("\n\n");
        appInfo.append("JOB REQUIREMENTS:\n").append(details.requirements);
        
        appArea.setText(appInfo.toString());
        appPanel.add(new JScrollPane(appArea), BorderLayout.CENTER);
        tabbedPane.addTab("📋 Application", appPanel);
        
        // Tab 2: Interview Info (if exists)
        if (details.hasInterview) {
            JPanel intPanel = new JPanel(new BorderLayout());
            JTextArea intArea = new JTextArea();
            intArea.setEditable(false);
            intArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            StringBuilder intInfo = new StringBuilder();
            intInfo.append("INTERVIEW INFORMATION\n\n");
            intInfo.append("Interview Date: ").append(details.interviewDate).append("\n");
            intInfo.append("Interview Time: ").append(details.interviewTime).append("\n");
            intInfo.append("Location: ").append(details.interviewLocation).append("\n");
            intInfo.append("Type: ").append(details.interviewType).append("\n");
            intInfo.append("Status: ").append(details.interviewStatus).append("\n\n");
            
            if (details.meetingLink != null && !details.meetingLink.isEmpty()) {
                intInfo.append("Meeting Link:\n").append(details.meetingLink).append("\n\n");
            }
            
            if (details.interviewNotes != null && !details.interviewNotes.isEmpty()) {
                intInfo.append("Interview Notes:\n").append(details.interviewNotes);
            }
            
            intArea.setText(intInfo.toString());
            intPanel.add(new JScrollPane(intArea), BorderLayout.CENTER);
            tabbedPane.addTab("📅 Interview", intPanel);
        }
        
        // Tab 3: My Resume/Cover Letter
        JPanel resumePanel = new JPanel(new BorderLayout());
        JTextArea resumeArea = new JTextArea(details.myResume);
        resumeArea.setEditable(false);
        resumeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resumePanel.add(new JScrollPane(resumeArea), BorderLayout.CENTER);
        tabbedPane.addTab("📄 My Resume", resumePanel);
        
        detailsDialog.add(tabbedPane, BorderLayout.CENTER);
        
        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> detailsDialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        detailsDialog.add(btnPanel, BorderLayout.SOUTH);
        
        detailsDialog.setVisible(true);
    }
    
    private List<ApplicationDetails> getMyApplications() {
        List<ApplicationDetails> applications = new ArrayList<>();
        
        String sql = "SELECT a.application_id, a.status, a.application_date, " +
                     "j.title as job_title, j.location, j.salary_range, " +
                     "c.name as company_name, " +
                     "i.status as interview_status " +
                     "FROM application a " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "JOIN company c ON j.company_id = c.company_id " +
                     "LEFT JOIN interview i ON a.application_id = i.application_id " +
                     "WHERE a.seeker_id = ? " +
                     "ORDER BY a.application_date DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, activeSeeker.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ApplicationDetails app = new ApplicationDetails();
                app.applicationId = rs.getInt("application_id");
                app.jobTitle = rs.getString("job_title");
                app.companyName = rs.getString("company_name");
                app.status = rs.getString("status");
                app.interviewStatus = rs.getString("interview_status");
                if (app.interviewStatus == null) {
                    app.interviewStatus = "Not Scheduled";
                }
                app.applicationDate = rs.getString("application_date");
                applications.add(app);
            }
        } catch (SQLException e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
        
        return applications;
    }
    
    private ApplicationDetails getApplicationDetails(int applicationId) {
        String sql = "SELECT a.application_id, a.status, a.application_date, a.resume, " +
                     "j.title as job_title, j.location, j.salary_range, j.platform, j.requirement, " +
                     "c.name as company_name, " +
                     "i.interview_date, i.interview_time, i.location as int_location, " +
                     "i.type as int_type, i.meeting_link, i.notes, i.status as int_status " +
                     "FROM application a " +
                     "JOIN job j ON a.job_id = j.job_id " +
                     "JOIN company c ON j.company_id = c.company_id " +
                     "LEFT JOIN interview i ON a.application_id = i.application_id " +
                     "WHERE a.application_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, applicationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ApplicationDetails details = new ApplicationDetails();
                details.applicationId = rs.getInt("application_id");
                details.jobTitle = rs.getString("job_title");
                details.companyName = rs.getString("company_name");
                details.location = rs.getString("location");
                details.salary = rs.getString("salary_range");
                details.platform = rs.getString("platform");
                details.requirements = rs.getString("requirement");
                details.status = rs.getString("status");
                details.applicationDate = rs.getString("application_date");
                details.myResume = rs.getString("resume");
                
                // Interview details
                details.interviewDate = rs.getString("interview_date");
                details.hasInterview = (details.interviewDate != null);
                
                if (details.hasInterview) {
                    details.interviewTime = rs.getString("interview_time");
                    details.interviewLocation = rs.getString("int_location");
                    details.interviewType = rs.getString("int_type");
                    details.meetingLink = rs.getString("meeting_link");
                    details.interviewNotes = rs.getString("notes");
                    details.interviewStatus = rs.getString("int_status");
                } else {
                    details.interviewStatus = "Not Scheduled";
                }
                
                return details;
            }
        } catch (SQLException e) {
            System.out.println("Error loading details: " + e.getMessage());
        }
        
        return null;
    }
    
    // Helper class to hold application details
    private static class ApplicationDetails {
        int applicationId;
        String jobTitle;
        String companyName;
        String location;
        String salary;
        String platform;
        String requirements;
        String status;
        String applicationDate;
        String myResume;
        
        boolean hasInterview;
        String interviewDate;
        String interviewTime;
        String interviewLocation;
        String interviewType;
        String meetingLink;
        String interviewNotes;
        String interviewStatus;
    }
}
