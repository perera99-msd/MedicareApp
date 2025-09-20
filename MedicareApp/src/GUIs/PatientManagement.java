package GUIs;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.EventQueue;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;

public class PatientManagement extends JFrame {
    private Connection connection;
    private JTextField txtId, txtName, txtAge, txtAddress, txtPhone, txtEmail, txtSearchId;
    private JTable patientTable;

    public PatientManagement() {
        // Initialize components
        initializeComponents();

        // Initialize database connection
        initializeDatabaseConnection();

        // Refresh patient table to display current data
        refreshPatientTable();
    }

    private void initializeComponents() {
        // Set up frame with a smaller size and title
        setTitle("Patient Management System");
        setSize(750, 600);  // Optimized frame size
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set frame background color
        getContentPane().setBackground(new Color(250, 250, 250));

        // Create tabbed pane for multiple panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Add/Update Patient
        JPanel addUpdatePanel = createAddUpdatePanel();

        // Panel for displaying Patient List
        JPanel patientListPanel = createPatientListPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add / Update Patient", addUpdatePanel);
        tabbedPane.addTab("Patient List", patientListPanel);

        // Add the tabbed pane to the main frame
        add(tabbedPane, BorderLayout.CENTER);

        // Add a "Back" button at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setBackground(new Color(250, 250, 250));

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Smaller font size
        btnBack.setBackground(new Color(85, 133, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(100, 40));  // Smaller size for the button
        btnBack.addActionListener(e -> goBackToMainFrame());
        buttonPanel.add(btnBack);

        // Add the button panel to the bottom of the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createAddUpdatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(createLabel("ID:"));
        txtId = createTextField();
        inputPanel.add(txtId);

        inputPanel.add(createLabel("Name:"));
        txtName = createTextField();
        inputPanel.add(txtName);

        inputPanel.add(createLabel("Age:"));
        txtAge = createTextField();
        inputPanel.add(txtAge);

        inputPanel.add(createLabel("Address:"));
        txtAddress = createTextField();
        inputPanel.add(txtAddress);

        inputPanel.add(createLabel("Phone:"));
        txtPhone = createTextField();
        inputPanel.add(txtPhone);

        inputPanel.add(createLabel("Email:"));
        txtEmail = createTextField();
        inputPanel.add(txtEmail);

        // Button Panel for Add, Update, Delete, Clear
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton(buttonPanel, "Add", e -> addPatient());
        addButton(buttonPanel, "Update", e -> updatePatient());
        addButton(buttonPanel, "Delete", e -> deletePatient());
        addButton(buttonPanel, "Clear", e -> clearInputFields());

        // Add input fields panel and buttons to the main panel
        panel.add(inputPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createPatientListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Patient search field and button
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchPanel.add(createLabel("Search by ID:"));
        txtSearchId = new JTextField(12);
        searchPanel.add(txtSearchId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(85, 133, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchPatient());
        searchPanel.add(btnSearch);

        // Patient table setup
        patientTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Age", "Address", "Phone", "Email"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(patientTable);

        // Add search panel and table to the patient list panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private void addButton(JPanel panel, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(new Color(85, 133, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.addActionListener(actionListener);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 110, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(85, 133, 255));
            }
        });

        panel.add(button);
    }

    private void initializeDatabaseConnection() {
        try {
            // Use correct database URL, username, and password
            String url = "jdbc:mysql://localhost:3306/medicareapp";
            String username = "root"; // Update with your MySQL username
            String password = ""; // Update with your MySQL password
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addPatient() {
        try {
            String query = "INSERT INTO patients (name, age, address, phone, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtAge.getText());
            pst.setString(3, txtAddress.getText());
            pst.setString(4, txtPhone.getText());
            pst.setString(5, txtEmail.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient added successfully!");

            // Send email to the patient using the email they provided
            sendEmail(txtEmail.getText()); // Send email to the patient's email address

            clearInputFields();
            refreshPatientTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to send email
    private void sendEmail(String recipientEmail) {
        // SMTP Server Information
        String host = "smtp.gmail.com";
        String port = "587"; // Use "465" for SSL
        final String username = "sithijasulank789@gmail.com"; // Your email
        final String password = "qcom resh mjyp rhgv"; // Use an App Password if using Gmail

        // SMTP Server Properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS security
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Authenticate and start a session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome to the Medicare System");
            message.setText("Hello,\n\nYour registration is successful! Welcome to the Medicare system. We are happy to have you with us.\n\nBest Regards,\nMedicare Team");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void updatePatient() {
        // Update patient code here
    }

    private void deletePatient() {
        // Delete patient code here
    }

    private void searchPatient() {
        // Search patient code here
    }

    private void clearInputFields() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtSearchId.setText("");
    }

    private void refreshPatientTable() {
        try {
            String query = "SELECT * FROM patients";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void goBackToMainFrame() {
        // Close the current window and show the MainFrame
        this.dispose(); // Close the current window (PatientManagement)
        new MainFrame(); // Open the MainFrame
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new PatientManagement().setVisible(true));
    }
}
