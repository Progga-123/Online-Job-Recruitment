package onlinejobrecruitment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton registerButton;
    
    // Map of creators
    private Map<String, DialogCreator> creators;

    public LoginFrame() {
        // Initialize creators map
        creators = new HashMap<>();
        creators.put("Job Seeker", new JobSeekerDialogCreator());
        creators.put("Company", new CompanyDialogCreator());
        creators.put("Admin", new AdminDialogCreator());
        
        setTitle("Online Job Portal - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("🎯 Online Job Recruitment System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Main Panel - Login Form
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Role Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        String[] roles = {"Job Seeker", "Company", "Admin"};
        userTypeCombo = new JComboBox<>(roles);
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        mainPanel.add(userTypeCombo, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 13));
        mainPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 13));
        mainPanel.add(passField, gbc);

        // Buttons
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 8, 8, 8);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom Info Panel
        JPanel infoPanel = new JPanel();
        JLabel infoLabel = new JLabel("New user? Click Register to create an account");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        add(infoPanel, BorderLayout.SOUTH);

        // Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
        
        // Allow Enter key to login
        passField.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String role = (String) userTypeCombo.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both email and password!", 
                "Missing Credentials", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (role.equals("Company")) {
            Company comp = new Company(email, password);
            if (comp.login()) {
                JOptionPane.showMessageDialog(this, "Welcome " + comp.getName() + "!");
                new CompanyDashboard(comp);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Company Credentials", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else if (role.equals("Job Seeker")) {
            JobSeeker seeker = new JobSeeker(email, password);
            if (seeker.login()) {
                JOptionPane.showMessageDialog(this, "Welcome " + seeker.getName() + "!");
                new JobSeekerDashboard(seeker);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Job Seeker Credentials", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else if (role.equals("Admin")) {
            Admin admin = new Admin(email, password);
            if (admin.login()) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
                new AdminDashboard(admin);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Admin Credentials", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // FACTORY METHOD PATTERN USAGE
    private void handleRegister() {
        String role = (String) userTypeCombo.getSelectedItem();
        
        // Get creator and use it (creator uses factory method internally)
        DialogCreator creator = creators.get(role);
        if (creator != null) {
            creator.showDialog(this);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

// ============================================
// COMPLETE FACTORY METHOD PATTERN SUMMARY
// ============================================
/*
 * ✅ 1. PRODUCT INTERFACE: RegistrationDialog
 *    - display()
 *    - validateInput()
 *    - getUserType()
 * 
 * ✅ 2. CONCRETE PRODUCTS: 
 *    - JobSeekerRegistrationDialog implements RegistrationDialog
 *    - CompanyRegistrationDialog implements RegistrationDialog
 * 
 * ✅ 3. ABSTRACT CREATOR: DialogCreator
 *    - abstract createDialog() - Factory Method
 *    - showDialog() - Template Method
 * 
 * ✅ 4. CONCRETE CREATORS:
 *    - JobSeekerDialogCreator extends DialogCreator
 *    - CompanyDialogCreator extends DialogCreator  
 *    - AdminDialogCreator extends DialogCreator
 * 
 * ✅ 5. CLIENT: LoginFrame
 *    - Uses Map<String, DialogCreator>
 *    - Depends on abstractions, not concrete classes
 * 
 * Benefits:
 * ✓ Open/Closed Principle
 * ✓ Single Responsibility  
 * ✓ Dependency Inversion
 * ✓ Easy to extend with new user types
 */