package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;

public class JobSeekerRegistrationDialog extends JDialog implements RegistrationDialog {
    
    public JobSeekerRegistrationDialog(JFrame parent) {
        super(parent, "Job Seeker Registration", true);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Create Your Job Seeker Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Full Name
        formPanel.add(new JLabel("Full Name: *"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);
        
        // Email
        formPanel.add(new JLabel("Email: *"));
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
        formPanel.add(new JLabel("Phone: *"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);
        
        // Address
        formPanel.add(new JLabel("Address: *"));
        JTextField addressField = new JTextField();
        formPanel.add(addressField);
        
        // Experience
        formPanel.add(new JLabel("Experience:"));
        JTextField experienceField = new JTextField("0 years");
        formPanel.add(experienceField);
        
        // Skills
        formPanel.add(new JLabel("Skills:"));
        JTextField skillsField = new JTextField();
        formPanel.add(skillsField);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        registerButton.setPreferredSize(new Dimension(120, 35));
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
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String phone = phoneField.getText().trim();
                    String address = addressField.getText().trim();
                    String experience = experienceField.getText().trim();
                    String skills = skillsField.getText().trim();
                    
                    JobSeeker seeker = new JobSeeker(name, email, password, phone, 
                                                    address, experience, skills);
                    
                    if (seeker.register()) {
                        JOptionPane.showMessageDialog(this,
                            "✅ Registration Successful!\n\n" +
                            "Welcome, " + name + "!\n" +
                            "You can now login with your email and password.",
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
        Component[] components = ((JPanel)getContentPane().getComponent(1)).getComponents();
        JTextField nameField = (JTextField) components[1];
        JTextField emailField = (JTextField) components[3];
        JPasswordField passwordField = (JPasswordField) components[5];
        JPasswordField confirmPasswordField = (JPasswordField) components[7];
        JTextField phoneField = (JTextField) components[9];
        JTextField addressField = (JTextField) components[11];
        
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || 
            phone.isEmpty() || address.isEmpty()) {
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
        return "Job Seeker";
    }
}

