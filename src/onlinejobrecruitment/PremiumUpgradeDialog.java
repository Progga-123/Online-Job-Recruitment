/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for upgrading to premium features
 */
public class PremiumUpgradeDialog extends JDialog {
    private JobSeeker activeSeeker;
    
    public PremiumUpgradeDialog(JFrame parent, JobSeeker seeker) {
        super(parent, "Upgrade to Premium", true);
        this.activeSeeker = seeker;
        
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 215, 0)); // Gold color
        JLabel titleLabel = new JLabel("👑 Upgrade to Premium Features");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Current Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Current Premium Status"));
        
        JTextArea statusArea = new JTextArea(3, 40);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Arial", Font.PLAIN, 12));
        statusArea.setText(PremiumManager.getPremiumStatus(seeker.getId()));
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        // Features Panel
        JPanel featuresPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Review Feature Card
        JPanel reviewCard = createFeatureCard(
            "⭐ Company Review Feature",
            "Write and post reviews for companies you've worked with or interviewed at. Help other job seekers make informed decisions!",
            "$9.99",
            "REVIEW_FEATURE",
            PremiumManager.hasFeature(seeker.getId(), "REVIEW_FEATURE")
        );
        
        // Profile Edit Feature Card
        JPanel profileCard = createFeatureCard(
            "👑 Profile Edit Feature",
            "Edit and update your profile anytime. Keep your information fresh and stand out with customization options!",
            "$14.99",
            "PROFILE_EDIT",
            PremiumManager.hasFeature(seeker.getId(), "PROFILE_EDIT")
        );
        
        featuresPanel.add(reviewCard);
        featuresPanel.add(profileCard);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(featuresPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createFeatureCard(String title, String description, 
                                     String price, String featureType, boolean owned) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setBackground(card.getBackground());
        card.add(descArea, BorderLayout.CENTER);
        
        // Bottom panel with price and button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        if (owned) {
            JLabel ownedLabel = new JLabel("✅ OWNED");
            ownedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            ownedLabel.setForeground(new Color(46, 204, 113));
            bottomPanel.add(ownedLabel);
        } else {
            JLabel priceLabel = new JLabel(price);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
            priceLabel.setForeground(new Color(52, 152, 219));
            
            JButton purchaseButton = new JButton("Purchase");
            purchaseButton.setFont(new Font("Arial", Font.BOLD, 13));
            purchaseButton.setBackground(new Color(46, 204, 113));
            purchaseButton.setForeground(Color.WHITE);
            
            purchaseButton.addActionListener(e -> {
                purchaseFeature(featureType, price);
            });
            
            bottomPanel.add(priceLabel);
            bottomPanel.add(purchaseButton);
        }
        
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void purchaseFeature(String featureType, String priceStr) {
        double amount = Double.parseDouble(priceStr.replace("$", ""));
        
        // Show payment dialog
        JDialog paymentDialog = new JDialog(this, "Complete Payment", true);
        paymentDialog.setSize(400, 300);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        infoPanel.add(new JLabel("Feature: " + featureType));
        infoPanel.add(new JLabel("Amount: $" + amount));
        infoPanel.add(new JLabel("Payment Method: Fake Payment (Demo)"));
        infoPanel.add(new JLabel("Status: Ready to Process"));
        
        JLabel noteLabel = new JLabel("<html><i>Note: This is a simulated payment for demo purposes only.</i></html>");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoPanel.add(noteLabel);
        
        paymentDialog.add(infoPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton payButton = new JButton("✅ Complete Payment");
        JButton cancelButton = new JButton("Cancel");
        
        payButton.setBackground(new Color(46, 204, 113));
        payButton.setForeground(Color.WHITE);
        payButton.setFont(new Font("Arial", Font.BOLD, 13));
        
        payButton.addActionListener(e -> {
            boolean success = PremiumManager.purchaseFeature(activeSeeker.getId(), featureType, amount);
            
            if (success) {
                JOptionPane.showMessageDialog(paymentDialog,
                    "✅ Payment Successful!\n\n" +
                    "Feature: " + featureType + "\n" +
                    "Amount Paid: $" + amount + "\n" +
                    "Status: PAID\n\n" +
                    "Your premium feature has been activated!",
                    "Payment Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                paymentDialog.dispose();
                dispose(); // Close upgrade dialog
                
                // Reopen to show updated status
                new PremiumUpgradeDialog((JFrame)getOwner(), activeSeeker);
            } else {
                JOptionPane.showMessageDialog(paymentDialog,
                    "Feature already purchased or payment failed.",
                    "Payment Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> paymentDialog.dispose());
        
        btnPanel.add(payButton);
        btnPanel.add(cancelButton);
        paymentDialog.add(btnPanel, BorderLayout.SOUTH);
        
        paymentDialog.setVisible(true);
    }
}