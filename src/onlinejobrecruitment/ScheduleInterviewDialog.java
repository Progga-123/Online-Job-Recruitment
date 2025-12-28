/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;



import javax.swing.*;
import java.awt.*;

/**
 * Dialog for scheduling interviews with candidates
 */
public class ScheduleInterviewDialog extends JDialog {
    private int applicationId;
    private int companyId;
    private int seekerId;
    private String candidateName;
    private String jobTitle;
    
    // Constructor for JDialog parent
    public ScheduleInterviewDialog(JDialog parent, int applicationId, int companyId, 
                                   int seekerId, String candidateName, String jobTitle) {
        super(parent, "Schedule Interview", true);
        this.applicationId = applicationId;
        this.companyId = companyId;
        this.seekerId = seekerId;
        this.candidateName = candidateName;
        this.jobTitle = jobTitle;
        
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("📅 Schedule Interview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Candidate Information"));
        infoPanel.add(new JLabel("  Candidate: " + candidateName));
        infoPanel.add(new JLabel("  Job Position: " + jobTitle));
        infoPanel.add(new JLabel("  Application ID: " + applicationId));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Interview Date
        formPanel.add(new JLabel("Interview Date: *"));
        JTextField dateField = new JTextField("2025-12-20");
        formPanel.add(dateField);
        
        // Interview Time
        formPanel.add(new JLabel("Interview Time: *"));
        JTextField timeField = new JTextField("10:00:00");
        formPanel.add(timeField);
        
        // Interview Type
        formPanel.add(new JLabel("Interview Type: *"));
        String[] types = {"In-person", "Virtual", "Phone"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        formPanel.add(typeCombo);
        
        // Location
        formPanel.add(new JLabel("Location:"));
        JTextField locationField = new JTextField("Company Office");
        formPanel.add(locationField);
        
        // Meeting Link
        formPanel.add(new JLabel("Meeting Link:"));
        JTextField linkField = new JTextField("https://meet.google.com/...");
        formPanel.add(linkField);
        
        // Notes Label
        formPanel.add(new JLabel("Interview Notes:"));
        formPanel.add(new JLabel("")); // Empty cell
        
        // Notes Text Area
        JTextArea notesArea = new JTextArea(4, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setText("Please bring your portfolio and relevant documents.");
        JScrollPane notesScroll = new JScrollPane(notesArea);
        
        // Combine info and form
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(notesScroll, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton scheduleButton = new JButton("✅ Schedule Interview");
        JButton cancelButton = new JButton("Cancel");
        
        scheduleButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        scheduleButton.setPreferredSize(new Dimension(180, 35));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        scheduleButton.setBackground(new Color(46, 204, 113));
        scheduleButton.setForeground(Color.WHITE);
        
        buttonPanel.add(scheduleButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        scheduleButton.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
                String time = timeField.getText().trim();
                String type = (String) typeCombo.getSelectedItem();
                String location = locationField.getText().trim();
                String link = linkField.getText().trim();
                String notes = notesArea.getText().trim();
                
                // Validation
                if (date.isEmpty() || time.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter date and time!",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Schedule the interview
                boolean success = InterviewManager.scheduleInterview(
                    applicationId, companyId, seekerId,
                    date, time, location, type, link, notes
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Interview Scheduled Successfully!\n\n" +
                        "Candidate: " + candidateName + "\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time + "\n" +
                        "Type: " + type + "\n\n" +
                        "The candidate will be notified.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to schedule interview. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        setVisible(true);
    }
}