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
 * Dialog for managing company subscriptions
 */
public class SubscriptionDialog extends JDialog {
    private JobSeeker activeSeeker;
    private JTable companyTable;
    private DefaultTableModel tableModel;
    
    public SubscriptionDialog(JFrame parent, JobSeeker seeker) {
        super(parent, "Manage Company Subscriptions", true);
        this.activeSeeker = seeker;
        
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Subscribe to companies to get notified of new jobs", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Company ID", "Company Name", "Location", "Subscribed"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only checkbox is editable
            }
        };
        
        companyTable = new JTable(tableModel);
        companyTable.getColumnModel().getColumn(3).setMaxWidth(100);
        JScrollPane scrollPane = new JScrollPane(companyTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Changes");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        saveButton.addActionListener(e -> saveSubscriptions());
        closeButton.addActionListener(e -> dispose());
        
        // Load data
        loadCompanies();
        
        setVisible(true);
    }
    
    private void loadCompanies() {
        tableModel.setRowCount(0);
        List<Integer> subscribedCompanies = SubscriptionManager.getSubscribedCompanies(activeSeeker.getId());
        
        String sql = "SELECT company_id, name, location FROM company ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int companyId = rs.getInt("company_id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                boolean isSubscribed = subscribedCompanies.contains(companyId);
                
                Object[] row = {companyId, name, location, isSubscribed};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading companies: " + e.getMessage());
        }
    }
    
    private void saveSubscriptions() {
        int changeCount = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int companyId = (int) tableModel.getValueAt(i, 0);
            boolean isChecked = (boolean) tableModel.getValueAt(i, 3);
            boolean wasSubscribed = SubscriptionManager.isSubscribed(activeSeeker.getId(), companyId);
            
            if (isChecked && !wasSubscribed) {
                // Subscribe
                if (activeSeeker.subscribeToCompany(companyId)) {
                    changeCount++;
                }
            } else if (!isChecked && wasSubscribed) {
                // Unsubscribe
                if (activeSeeker.unsubscribeFromCompany(companyId)) {
                    changeCount++;
                }
            }
        }
        
        if (changeCount > 0) {
            JOptionPane.showMessageDialog(this, 
                "Subscription changes saved! (" + changeCount + " updates)", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No changes made.");
        }
        
        dispose();
    }
}