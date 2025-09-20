package GUIs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MonthlyReports extends JFrame {
    private Connection connection;
    private JComboBox<Integer> cmbMonth, cmbYear;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public MonthlyReports() {
        // Initialize components
        initializeComponents();

        // Initialize database connection
        initializeDatabaseConnection();
    }

    private void initializeComponents() {
        // Set up frame
        setTitle("Monthly Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Panel for selecting month and year, and displaying report
        JPanel reportPanel = createReportPanel();
        add(reportPanel, BorderLayout.CENTER);

        // Create and add the Back button to the bottom of the frame
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setBackground(new Color(85, 133, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(120, 40));

        // Add action listener to handle the back button click
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBackToMainFrame();
            }
        });

        // Add the back button at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Month and Year Selection Panel
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        selectPanel.add(new JLabel("Select Month:"));
        cmbMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cmbMonth.addItem(i);
        }
        selectPanel.add(cmbMonth);

        selectPanel.add(new JLabel("Select Year:"));
        cmbYear = new JComboBox<>();
        for (int i = 2020; i <= 2025; i++) {
            cmbYear.addItem(i);
        }
        selectPanel.add(cmbYear);

        // Button to generate report
        JButton btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateMonthlyReport();
            }
        });
        selectPanel.add(btnGenerateReport);

        panel.add(selectPanel);

        // Table to display the report
        String[] columnNames = {"Report Type", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane);

        return panel;
    }

    private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/medicareapp";
            String username = "root"; // Update with your MySQL username
            String password = ""; // Update with your MySQL password
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateMonthlyReport() {
        int month = (int) cmbMonth.getSelectedItem();
        int year = (int) cmbYear.getSelectedItem();

        // Clear previous data in table
        tableModel.setRowCount(0);

        // Fetch data and add to table
        addReportRow("Revenue Report", getAppointmentRevenue(month, year));
        addReportRow("Patient Visits Report", getPatientVisits(month, year));
        addReportRow("Inventory Usage Report", getInventoryUsage(month, year));
    }

    private void addReportRow(String reportType, String details) {
        tableModel.addRow(new Object[]{reportType, details});
    }

    private String getAppointmentRevenue(int month, int year) {
        String query = "SELECT SUM(p.price) AS total_revenue " +
                "FROM appointments a " +
                "JOIN pharmacy_inventory p ON a.appointment_id = p.id " +
                "WHERE MONTH(a.appointment_date) = ? AND YEAR(a.appointment_date) = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, month);
            pst.setInt(2, year);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return "Total Appointment Revenue: " + rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No data available for revenue.";
    }

    private String getPatientVisits(int month, int year) {
        String query = "SELECT p.name AS patient_name, COUNT(a.appointment_id) AS visit_count " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "WHERE MONTH(a.appointment_date) = ? AND YEAR(a.appointment_date) = ? " +
                "GROUP BY p.name";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, month);
            pst.setInt(2, year);
            ResultSet rs = pst.executeQuery();
            StringBuilder visitsReport = new StringBuilder();
            while (rs.next()) {
                visitsReport.append(rs.getString("patient_name"))
                        .append(": ")
                        .append(rs.getInt("visit_count"))
                        .append(" visits\n");
            }
            if (visitsReport.length() == 0) {
                return "No visits data available.";
            }
            return visitsReport.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error retrieving patient visits.";
    }

    private String getInventoryUsage(int month, int year) {
        String query = "SELECT pi.medicine_name, SUM(iu.quantity_used) AS total_usage " +
                "FROM inventory_usage iu " +
                "JOIN pharmacy_inventory pi ON iu.inventory_id = pi.id " +
                "WHERE MONTH(iu.usage_date) = ? AND YEAR(iu.usage_date) = ? " +
                "GROUP BY pi.medicine_name";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, month);
            pst.setInt(2, year);
            ResultSet rs = pst.executeQuery();
            StringBuilder usageReport = new StringBuilder();
            while (rs.next()) {
                usageReport.append(rs.getString("medicine_name"))
                        .append(": ")
                        .append(rs.getInt("total_usage"))
                        .append(" units used\n");
            }
            if (usageReport.length() == 0) {
                return "No inventory usage data available.";
            }
            return usageReport.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error retrieving inventory usage.";
    }

    private void goBackToMainFrame() {
        // Close current frame (MonthlyReports)
        this.dispose();

        // Open the MainFrame
        new MainFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MonthlyReports().setVisible(true));
    }
}
