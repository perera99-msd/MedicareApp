package GUIs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private JLabel lblImage;

    public LoginFrame() {
        // Set the title and size of the Login Frame
        setTitle("Login - Medicare Plus");
        setSize(400, 400);  // Increased size for better UI
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the layout to BorderLayout
        setLayout(new BorderLayout(10, 10)); // Add padding between components

        // Create a panel for the image (optional)
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setIcon(new ImageIcon("C:\\Users\\msdpe\\Desktop\\OOP Assignment\\MedicareApp\\src\\GUIs\\1.jpeg")); // Set your image path here
        add(lblImage, BorderLayout.NORTH); // Add image at the top of the window

        // Create the login form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 20)); // 3 rows, 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the panel
        formPanel.setOpaque(false);  // Make panel background transparent

        // Add Username label and text field
        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for better appearance
        txtUsername.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Border around the text field
        formPanel.add(txtUsername);

        // Add Password label and password field
        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        formPanel.add(txtPassword);

        // Add Login button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(70, 130, 180));  // Blue color
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
        formPanel.add(new JLabel());  // Empty space to balance layout
        formPanel.add(btnLogin);

        // Add the form panel to the center of the frame
        add(formPanel, BorderLayout.CENTER);

        // Add a label for error messages (initially hidden)
        lblError = new JLabel("Invalid username or password", JLabel.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setVisible(false); // Hide the error message initially
        add(lblError, BorderLayout.SOUTH);  // Place error message at the bottom

        // Show the login window
        setVisible(true);
    }

    private void authenticateUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // Simple authentication check (hardcoded)
        if (username.equals("admin") && password.equals("admin123")) {
            // Close login window and open the main frame
            this.dispose();
            new MainFrame().setVisible(true);
        } else {
            // Show error message
            lblError.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
