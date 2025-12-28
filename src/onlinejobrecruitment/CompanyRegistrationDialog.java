package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;

public class CompanyRegistrationDialog extends JDialog implements RegistrationDialog {
    
    public CompanyRegistrationDialog(JFrame parent) {
        super(parent, "Company Registration", true);
        setSize(550, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Register Your Company");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        // Company Name
        formPanel.add(new JLabel("Company Name: *"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);
        
        // Email
        formPanel.add(new JLabel("Company Email: *"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        // Password
        formPanel.add(new JLabel("Password: *"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        // Confirm Password
        formPanel.add(new JLabel("Confirm Password: *"));
        JPasswordField confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        // Phone
        formPanel.add(new JLabel("Phone Number: *"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);
        
        // Location
        formPanel.add(new JLabel("Location: *"));
        JTextField locationField = new JTextField();
        formPanel.add(locationField);
        
        // Website
        formPanel.add(new JLabel("Website:"));
        JTextField websiteField = new JTextField("https://");
        formPanel.add(websiteField);
        
        // Company Details (label only)
        formPanel.add(new JLabel("Company Details:"));
        formPanel.add(new JLabel("")); // Empty cell
        
        add(formPanel, BorderLayout.CENTER);
        
        // Details Text Area Panel
        JPanel detailsPanel = new JPanel(new BorderLayout(5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        
        JTextArea detailsArea = new JTextArea(4, 40);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Describe your company...");
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);
        
        // Combine form and details
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton registerButton = new JButton("Register Company");
        JButton cancelButton = new JButton("Cancel");
        
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        registerButton.setPreferredSize(new Dimension(160, 35));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        // Info Label
        JLabel infoLabel = new JLabel("* Required fields", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(infoLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Register Button Action
        registerButton.addActionListener(e -> {
            if (validateInput()) {
                try {
                    Component[] formComponents = ((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).getComponents();
                    String name = ((JTextField) formComponents[1]).getText().trim();
                    String email = ((JTextField) formComponents[3]).getText().trim();
                    String password = new String(((JPasswordField) formComponents[5]).getPassword());
                    String phone = ((JTextField) formComponents[9]).getText().trim();
                    String location = ((JTextField) formComponents[11]).getText().trim();
                    String website = ((JTextField) formComponents[13]).getText().trim();
                    String details = detailsArea.getText().trim();
                    
                    Company company = new Company(name, email, password, phone, 
                                                location, website, details);
                    
                    if (company.register()) {
                        JOptionPane.showMessageDialog(this,
                            "✅ Company Registration Successful!\n\n" +
                            "Welcome, " + name + "!\n" +
                            "You can now login and start posting jobs.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Registration failed. Email might already be registered.",
                            "Registration Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    // Implementing RegistrationDialog interface
    @Override
    public void display() {
        setVisible(true);
    }
    
    @Override
    public boolean validateInput() {
        Component[] formComponents = ((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).getComponents();
        String name = ((JTextField) formComponents[1]).getText().trim();
        String email = ((JTextField) formComponents[3]).getText().trim();
        String password = new String(((JPasswordField) formComponents[5]).getPassword());
        String confirmPassword = new String(((JPasswordField) formComponents[7]).getPassword());
        String phone = ((JTextField) formComponents[9]).getText().trim();
        String location = ((JTextField) formComponents[11]).getText().trim();
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || 
            phone.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields!",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address!",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long!",
                "Weak Password",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match!",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getUserType() {
        return "Company";
    }
}
