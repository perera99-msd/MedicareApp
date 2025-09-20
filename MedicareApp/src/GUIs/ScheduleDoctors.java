package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ScheduleDoctors extends JFrame {
    private Connection connection;
    private JTextField txtDate, txtTime, txtPatientName, txtSearchScheduleId, txtScheduleId;
    private JComboBox<Integer> cmbDoctorId; // ComboBox for doctor selection
    private JTable scheduleTable;

    public ScheduleDoctors() {
        // Initialize database connection first
        initializeDatabaseConnection();

        // Initialize components
        initializeComponents();

        // Refresh schedule table to display current data
        refreshScheduleTable();
    }

    private void initializeComponents() {
        // Set up frame
        setTitle("Doctor Scheduling System");
        setSize(750, 600);  // Optimized frame size
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set frame background color
        getContentPane().setBackground(new Color(250, 250, 250));

        // Create tabbed pane for multiple panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Add/Update Schedule
        JPanel addUpdateSchedulePanel = createAddUpdateSchedulePanel();

        // Panel for displaying Schedule List
        JPanel scheduleListPanel = createScheduleListPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add / Update Schedule", addUpdateSchedulePanel);
        tabbedPane.addTab("Schedule List", scheduleListPanel);

        // Add the tabbed pane to the main frame
        add(tabbedPane, BorderLayout.CENTER);

        // Create Back button and add it to the bottom panel
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setBackground(new Color(85, 133, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(100, 35)); // Make it smaller

        // Add hover effect for the Back button
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(new Color(70, 110, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(new Color(85, 133, 255));
            }
        });

        // Action listener to go back to the main screen
        btnBack.addActionListener(e -> {
            // Close current frame (ScheduleDoctors)
            this.dispose();
            // Open the main frame (MainFrame)
            new MainFrame().setVisible(true);
        });

        // Panel for the Back button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245)); // Same background as other panels
        bottomPanel.add(btnBack);

        // Add the Bottom Panel with Back button to the bottom of the main frame
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createAddUpdateSchedulePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10)); // 6 rows for new "Schedule ID"
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(createLabel("Schedule ID:"));
        txtScheduleId = createTextField();
        inputPanel.add(txtScheduleId);

        inputPanel.add(createLabel("Doctor ID:"));
        cmbDoctorId = new JComboBox<>();
        loadDoctorIdsIntoComboBox(); // Load doctor IDs from database into ComboBox
        inputPanel.add(cmbDoctorId);

        inputPanel.add(createLabel("Date (YYYY-MM-DD):"));
        txtDate = createTextField();
        inputPanel.add(txtDate);

        inputPanel.add(createLabel("Time (HH:MM:SS):"));
        txtTime = createTextField();
        inputPanel.add(txtTime);

        inputPanel.add(createLabel("Patient Name:"));
        txtPatientName = createTextField();
        inputPanel.add(txtPatientName);

        // Button Panel for Add, Update, Delete, Clear
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton(buttonPanel, "Add Schedule", e -> addSchedule());
        addButton(buttonPanel, "Update Schedule", e -> updateSchedule());
        addButton(buttonPanel, "Delete Schedule", e -> deleteSchedule());
        addButton(buttonPanel, "Clear", e -> clearInputFields());

        // Add input fields panel and buttons to the main panel
        panel.add(inputPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createScheduleListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Schedule search field and button
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchPanel.add(createLabel("Search by Schedule ID:"));
        txtSearchScheduleId = new JTextField(12);
        searchPanel.add(txtSearchScheduleId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(85, 133, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchSchedule());
        searchPanel.add(btnSearch);

        // Schedule table setup
        scheduleTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Doctor ID", "Date", "Time", "Status", "Patient Name"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);

        // Add search panel and table to the schedule list panel
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
            System.out.println("Database connection successful!");  // Debugging line
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDoctorIdsIntoComboBox() {
        if (connection != null) {
            try {
                String query = "SELECT id FROM doctors"; // Assuming there's a "doctors" table with an "id" column
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    cmbDoctorId.addItem(rs.getInt("id"));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error loading doctor IDs: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Database connection is not established.");
        }
    }

    private void addSchedule() {
        try {
            String query = "INSERT INTO doctor_schedule (doctor_id, date, time, status, patient_name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, (Integer) cmbDoctorId.getSelectedItem()); // Use selected doctor ID
            pst.setString(2, txtDate.getText());
            pst.setString(3, txtTime.getText());
            pst.setString(4, "Scheduled");  // Default status is "Scheduled"
            pst.setString(5, txtPatientName.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Schedule added successfully!");
            clearInputFields();
            refreshScheduleTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSchedule() {
        try {
            String query = "UPDATE doctor_schedule SET date=?, time=?, patient_name=? WHERE doctor_id=? AND id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtDate.getText());
            pst.setString(2, txtTime.getText());
            pst.setString(3, txtPatientName.getText());
            pst.setInt(4, (Integer) cmbDoctorId.getSelectedItem()); // Use selected doctor ID
            pst.setInt(5, Integer.parseInt(txtScheduleId.getText())); // Use schedule ID for update
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Schedule updated successfully!");
            clearInputFields();
            refreshScheduleTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteSchedule() {
        try {
            String query = "DELETE FROM doctor_schedule WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtScheduleId.getText())); // Use schedule ID for deletion
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Schedule deleted successfully!");
            clearInputFields();
            refreshScheduleTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchSchedule() {
        try {
            String query = "SELECT * FROM doctor_schedule WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtSearchScheduleId.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                cmbDoctorId.setSelectedItem(rs.getInt("doctor_id"));
                txtDate.setText(rs.getString("date"));
                txtTime.setText(rs.getString("time"));
                txtPatientName.setText(rs.getString("patient_name"));
                txtScheduleId.setText(String.valueOf(rs.getInt("id"))); // Display schedule ID for updates
            } else {
                JOptionPane.showMessageDialog(null, "Schedule not found!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtScheduleId.setText("");
        cmbDoctorId.setSelectedIndex(0);
        txtDate.setText("");
        txtTime.setText("");
        txtPatientName.setText("");
        txtSearchScheduleId.setText("");
    }

    private void refreshScheduleTable() {
        try {
            String query = "SELECT * FROM doctor_schedule";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getInt("doctor_id"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        rs.getString("patient_name")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing schedule table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Ensure the frame is initialized and visible on the event dispatch thread
                ScheduleDoctors frame = new ScheduleDoctors();
                frame.setVisible(true); // Ensure the frame becomes visible
            }
        });
    }
}
