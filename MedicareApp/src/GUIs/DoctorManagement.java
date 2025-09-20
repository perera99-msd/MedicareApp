package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorManagement extends JFrame {
    private Connection connection;
    private JTextField txtId, txtName, txtSpecialization, txtPhone, txtSearchId;
    private JTable doctorTable;

    public DoctorManagement() {
        // Initialize components
        initializeComponents();

        // Initialize database connection
        initializeDatabaseConnection();

        // Refresh doctor table to display current data
        refreshDoctorTable();
    }

    private void initializeComponents() {
        // Set up frame with a smaller size and title
        setTitle("Doctor Management System");
        setSize(750, 600);  // Optimized frame size
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set frame background color
        getContentPane().setBackground(new Color(250, 250, 250));

        // Create tabbed pane for multiple panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Add/Update Doctor
        JPanel addUpdatePanel = createAddUpdatePanel();

        // Panel for displaying Doctor List
        JPanel doctorListPanel = createDoctorListPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add / Update Doctor", addUpdatePanel);
        tabbedPane.addTab("Doctor List", doctorListPanel);

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
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(createLabel("ID:"));
        txtId = createTextField();
        inputPanel.add(txtId);

        inputPanel.add(createLabel("Name:"));
        txtName = createTextField();
        inputPanel.add(txtName);

        inputPanel.add(createLabel("Specialization:"));
        txtSpecialization = createTextField();
        inputPanel.add(txtSpecialization);

        inputPanel.add(createLabel("Phone:"));
        txtPhone = createTextField();
        inputPanel.add(txtPhone);

        // Button Panel for Add, Update, Delete, Clear
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton(buttonPanel, "Add", e -> addDoctor());
        addButton(buttonPanel, "Update", e -> updateDoctor());
        addButton(buttonPanel, "Delete", e -> deleteDoctor());
        addButton(buttonPanel, "Clear", e -> clearInputFields());

        // Add input fields panel and buttons to the main panel
        panel.add(inputPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createDoctorListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Doctor search field and button
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
        btnSearch.addActionListener(e -> searchDoctor());
        searchPanel.add(btnSearch);

        // Doctor table setup
        doctorTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Specialization", "Phone"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(doctorTable);

        // Add search panel and table to the doctor list panel
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

    private void addDoctor() {
        try {
            String query = "INSERT INTO doctors (name, specialization, phone) VALUES (?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtSpecialization.getText());
            pst.setString(3, txtPhone.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor added successfully!");
            clearInputFields();
            refreshDoctorTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding doctor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateDoctor() {
        try {
            String query = "UPDATE doctors SET name=?, specialization=?, phone=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtSpecialization.getText());
            pst.setString(3, txtPhone.getText());
            pst.setInt(4, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor updated successfully!");
            clearInputFields();
            refreshDoctorTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating doctor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteDoctor() {
        try {
            String query = "DELETE FROM doctors WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor deleted successfully!");
            clearInputFields();
            refreshDoctorTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting doctor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchDoctor() {
        try {
            String query = "SELECT * FROM doctors WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtSearchId.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtId.setText(rs.getString("id"));
                txtName.setText(rs.getString("name"));
                txtSpecialization.setText(rs.getString("specialization"));
                txtPhone.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(null, "Doctor not found!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching doctor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtId.setText("");
        txtName.setText("");
        txtSpecialization.setText("");
        txtPhone.setText("");
        txtSearchId.setText("");
    }

    private void refreshDoctorTable() {
        try {
            String query = "SELECT * FROM doctors";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) doctorTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("phone")
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
        this.dispose(); // Close the current window (DoctorManagement)
        new MainFrame(); // Open the MainFrame
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new DoctorManagement().setVisible(true));
    }
}
