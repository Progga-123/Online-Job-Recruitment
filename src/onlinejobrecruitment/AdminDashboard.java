package onlinejobrecruitment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private Admin activeAdmin;
    private JTable companyTable;
    private JTable seekerTable;
    private DefaultTableModel companyModel;
    private DefaultTableModel seekerModel;

    public AdminDashboard(Admin admin) {
        this.activeAdmin = admin;
        setTitle("Admin Dashboard - " + admin.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // We use a TabbedPane to switch between views
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- TAB 1: COMPANY MANAGEMENT ---
        JPanel companyPanel = new JPanel(new BorderLayout());
        String[] companyCols = {"ID", "Name", "Email", "Location", "Phone"};
        companyModel = new DefaultTableModel(companyCols, 0);
        companyTable = new JTable(companyModel);
        
        JButton deleteCompanyBtn = new JButton("Delete Selected Company");
        deleteCompanyBtn.addActionListener(e -> deleteSelectedCompany());
        
        companyPanel.add(new JScrollPane(companyTable), BorderLayout.CENTER);
        companyPanel.add(deleteCompanyBtn, BorderLayout.SOUTH);
        
        // --- TAB 2: JOB SEEKER MANAGEMENT ---
        JPanel seekerPanel = new JPanel(new BorderLayout());
        String[] seekerCols = {"ID", "Name", "Email", "Skills", "Experience"};
        seekerModel = new DefaultTableModel(seekerCols, 0);
        seekerTable = new JTable(seekerModel);
        
        JButton deleteSeekerBtn = new JButton("Delete Selected Job Seeker");
        deleteSeekerBtn.addActionListener(e -> deleteSelectedSeeker());
        
        seekerPanel.add(new JScrollPane(seekerTable), BorderLayout.CENTER);
        seekerPanel.add(deleteSeekerBtn, BorderLayout.SOUTH);

        // Add tabs to main pane
        tabbedPane.addTab("Manage Companies", companyPanel);
        tabbedPane.addTab("Manage Job Seekers", seekerPanel);
        
        // Add Logout Button at the bottom of the frame
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        
        add(tabbedPane, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);

        // Initial Data Load
        refreshCompanyData();
        refreshSeekerData();

        setVisible(true);
    }

    private void refreshCompanyData() {
        companyModel.setRowCount(0); // Clear table
        List<Company> companies = activeAdmin.getAllCompanies();
        
        for (Company c : companies) {
            Object[] row = {c.getId(), c.getName(), c.getEmail(), c.getLocation(), c.getPhone()};
            companyModel.addRow(row);
        }
    }

    private void refreshSeekerData() {
        seekerModel.setRowCount(0); // Clear table
        List<JobSeeker> seekers = activeAdmin.getAllJobSeekers();
        
        for (JobSeeker s : seekers) {
            Object[] row = {s.getId(), s.getName(), s.getEmail(), s.getSkills(), s.getExperience()};
            seekerModel.addRow(row);
        }
    }

    private void deleteSelectedCompany() {
        int row = companyTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a company to delete!");
            return;
        }

        int id = (int) companyTable.getValueAt(row, 0);
        String name = (String) companyTable.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete " + name + "?\nThis will remove all their posted jobs!", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (activeAdmin.deleteCompany(id)) {
                JOptionPane.showMessageDialog(this, "Company Deleted.");
                refreshCompanyData(); // Reload table
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting company.");
            }
        }
    }

    private void deleteSelectedSeeker() {
        int row = seekerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job seeker to delete!");
            return;
        }

        int id = (int) seekerTable.getValueAt(row, 0);
        String name = (String) seekerTable.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete " + name + "?\nThis will remove their applications!", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (activeAdmin.deleteJobSeeker(id)) {
                JOptionPane.showMessageDialog(this, "Job Seeker Deleted.");
                refreshSeekerData(); // Reload table
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting seeker.");
            }
        }
    }
}