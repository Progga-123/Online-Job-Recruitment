package onlinejobrecruitment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog for viewing job notifications
 */
public class NotificationDialog extends JDialog {
    private JobSeeker activeSeeker;
    private JTable notificationTable;
    private DefaultTableModel tableModel;
    
    public NotificationDialog(JFrame parent, JobSeeker seeker) {
        super(parent, "Job Notifications", true);
        this.activeSeeker = seeker;
        
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Your Job Notifications", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Status", "Company", "Job Title", "Job ID", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        notificationTable = new JTable(tableModel);
        notificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set column widths
        notificationTable.getColumnModel().getColumn(0).setMaxWidth(50);
        notificationTable.getColumnModel().getColumn(1).setMaxWidth(80);
        notificationTable.getColumnModel().getColumn(4).setMaxWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton markAllReadButton = new JButton("Mark All as Read");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(markAllReadButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        markAllReadButton.addActionListener(e -> {
            activeSeeker.markAllNotificationsAsRead();
            loadNotifications();
            JOptionPane.showMessageDialog(this, "All notifications marked as read!");
        });
        
        closeButton.addActionListener(e -> dispose());
        
        // Load notifications
        loadNotifications();
        
        setVisible(true);
    }
    
    private void loadNotifications() {
        tableModel.setRowCount(0);
        List<Notification> notifications = activeSeeker.getNotifications();
        
        if (notifications.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No notifications yet. Subscribe to companies to receive updates!", 
                "No Notifications", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Notification notif : notifications) {
            String status = notif.isRead() ? "Read" : "NEW";
            Object[] row = {
                notif.getNotificationId(),
                status,
                notif.getCompanyName(),
                notif.getJobTitle(),
                notif.getJobId(),
                notif.getCreatedAt().toString().substring(0, 16) // Format date
            };
            tableModel.addRow(row);
            
            // Highlight unread notifications
            if (!notif.isRead()) {
                // Note: To actually highlight rows, you'd need a custom cell renderer
                // This is a simplified version
            }
        }
    }
}