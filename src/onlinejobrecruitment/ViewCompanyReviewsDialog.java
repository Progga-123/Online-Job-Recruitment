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
 * Dialog for companies to view their reviews
 */
public class ViewCompanyReviewsDialog extends JDialog {
    private Company activeCompany;
    private JTable reviewsTable;
    private DefaultTableModel tableModel;
    private JTextArea reviewTextArea;
    private JLabel ratingLabel;
    
    public ViewCompanyReviewsDialog(JFrame parent, Company company) {
        super(parent, "Company Reviews - " + company.getName(), true);
        this.activeCompany = company;
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel with rating
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(52, 152, 219));
        
        JLabel titleLabel = new JLabel("⭐ Company Reviews");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        ratingLabel = new JLabel();
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ratingLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(ratingLabel, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        // Top Panel - Reviews Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Reviews Received"));
        
        String[] columns = {"Review ID", "Reviewer", "Rating", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reviewsTable = new JTable(tableModel);
        reviewsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(reviewsTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        splitPane.setTopComponent(tablePanel);
        
        // Bottom Panel - Review Details
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Review Details"));
        
        reviewTextArea = new JTextArea();
        reviewTextArea.setEditable(false);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        reviewTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
        reviewTextArea.setText("Select a review to view details...");
        
        JScrollPane reviewScroll = new JScrollPane(reviewTextArea);
        detailsPanel.add(reviewScroll, BorderLayout.CENTER);
        
        splitPane.setBottomComponent(detailsPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton refreshButton = new JButton("🔄 Refresh");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        reviewsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSelectedReview();
            }
        });
        
        refreshButton.addActionListener(e -> loadReviews());
        closeButton.addActionListener(e -> dispose());
        
        // Load data
        loadReviews();
        
        setVisible(true);
    }
    
    private void loadReviews() {
        tableModel.setRowCount(0);
        reviewTextArea.setText("Loading reviews...");
        
        List<CompanyReview> reviews = ReviewManager.getCompanyReviews(activeCompany.getId());
        
        // Update rating label
        double avgRating = ReviewManager.getAverageRating(activeCompany.getId());
        int count = reviews.size();
        
        if (count > 0) {
            ratingLabel.setText(String.format("  Average: %.1f ⭐ (%d reviews)  ", avgRating, count));
        } else {
            ratingLabel.setText("  No reviews yet  ");
        }
        
        if (reviews.isEmpty()) {
            reviewTextArea.setText("No reviews have been posted yet.\n\n" +
                                  "Premium job seekers can write reviews for your company.");
            JOptionPane.showMessageDialog(this,
                "No reviews have been received for your company yet.",
                "No Reviews",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (CompanyReview review : reviews) {
            Object[] row = {
                review.getReviewId(),
                review.getSeekerName(),
                review.getStarRating(),
                review.getCreatedAt().toString().substring(0, 10)
            };
            tableModel.addRow(row);
        }
        
        reviewTextArea.setText("Select a review to view details...\n\n" +
                              String.format("Total Reviews: %d\nAverage Rating: %.1f / 5.0", 
                                          count, avgRating));
    }
    
    private void viewSelectedReview() {
        int selectedRow = reviewsTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int reviewId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Get the full review
        List<CompanyReview> reviews = ReviewManager.getCompanyReviews(activeCompany.getId());
        CompanyReview selectedReview = null;
        
        for (CompanyReview review : reviews) {
            if (review.getReviewId() == reviewId) {
                selectedReview = review;
                break;
            }
        }
        
        if (selectedReview != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════\n");
            sb.append("                REVIEW DETAILS\n");
            sb.append("═══════════════════════════════════════════════════\n\n");
            
            sb.append("Review ID: ").append(selectedReview.getReviewId()).append("\n");
            sb.append("Reviewer: ").append(selectedReview.getSeekerName()).append("\n");
            sb.append("Rating: ").append(selectedReview.getStarRating()).append(" (")
              .append(selectedReview.getRating()).append("/5)\n");
            sb.append("Date: ").append(selectedReview.getCreatedAt().toString().substring(0, 16)).append("\n\n");
            
            sb.append("───────────────────────────────────────────────────\n");
            sb.append("REVIEW TEXT:\n");
            sb.append("───────────────────────────────────────────────────\n\n");
            sb.append(selectedReview.getReviewText());
            
            reviewTextArea.setText(sb.toString());
            reviewTextArea.setCaretPosition(0);
        }
    }
}