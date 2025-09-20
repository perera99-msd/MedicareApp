package GUIs;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Properties;

public class ScheduleAppointments extends JFrame {
    private Connection connection;
    private JTextField txtAppointmentId, txtAppointmentDate, txtAppointmentTime, txtStatus;
    private JComboBox<Integer> cmbDoctorId, cmbPatientId;
    private JTable appointmentTable;

    public ScheduleAppointments() {
        // Initialize components
        initializeComponents();

        // Initialize database connection
        initializeDatabaseConnection();

        // Load patients and doctors into combo boxes
        loadPatientsIntoComboBox();
        loadDoctorsIntoComboBox();

        // Refresh appointment table to display current data
        refreshAppointmentTable();
    }

    private void initializeComponents() {
        // Set up frame
        setTitle("Appointment Booking System");
        setSize(800, 600);  // Optimized frame size
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set frame background color
        getContentPane().setBackground(new Color(250, 250, 250));

        // Create tabbed pane for multiple panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Add/Update Appointment
        JPanel addUpdateAppointmentPanel = createAddUpdateAppointmentPanel();

        // Panel for displaying Appointment List
        JPanel appointmentListPanel = createAppointmentListPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add / Update Appointment", addUpdateAppointmentPanel);
        tabbedPane.addTab("Appointment List", appointmentListPanel);

        // Add the tabbed pane to the main frame
        add(tabbedPane, BorderLayout.CENTER);

        // Create a Back button at the bottom of the frame
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(100, 30)); // Small size
        backButton.setBackground(new Color(85, 133, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> backAction());

        // Add hover effect
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(70, 110, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(85, 133, 255));
            }
        });

        backButtonPanel.add(backButton);
        add(backButtonPanel, BorderLayout.SOUTH); // Add the Back button at the bottom
    }

    private void backAction() {
        // Close the current ScheduleAppointments frame
        this.dispose();

        // Create and show MainFrame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    private JPanel createAddUpdateAppointmentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10)); // Increased rows to 6 for Appointment ID input
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(createLabel("Appointment ID:"));
        txtAppointmentId = createTextField(); // TextField for Appointment ID
        inputPanel.add(txtAppointmentId);

        inputPanel.add(createLabel("Patient ID:"));
        cmbPatientId = createComboBox();
        inputPanel.add(cmbPatientId);

        inputPanel.add(createLabel("Doctor ID:"));
        cmbDoctorId = createComboBox();
        inputPanel.add(cmbDoctorId);

        inputPanel.add(createLabel("Appointment Date (YYYY-MM-DD):"));
        txtAppointmentDate = createTextField();
        inputPanel.add(txtAppointmentDate);

        inputPanel.add(createLabel("Appointment Time (HH:MM:SS):"));
        txtAppointmentTime = createTextField();
        inputPanel.add(txtAppointmentTime);

        inputPanel.add(createLabel("Status:"));
        txtStatus = createTextField();
        inputPanel.add(txtStatus);

        // Button Panel for Add, Update, Delete, Clear
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton(buttonPanel, "Add Appointment", e -> addAppointment());
        addButton(buttonPanel, "Update Appointment", e -> updateAppointment());
        addButton(buttonPanel, "Delete Appointment", e -> deleteAppointment());
        addButton(buttonPanel, "Clear", e -> clearInputFields());

        // Add input fields panel and buttons to the main panel
        panel.add(inputPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createAppointmentListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Appointment table setup
        appointmentTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Patient ID", "Doctor ID", "Date", "Time", "Status"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);

        // Add the table to the appointment list panel
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

    private JComboBox<Integer> createComboBox() {
        return new JComboBox<>();
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
            String url = "jdbc:mysql://localhost:3306/medicareapp"; // Change database name as required
            String username = "root"; // Update with your MySQL username
            String password = ""; // Update with your MySQL password
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPatientsIntoComboBox() {
        try {
            String query = "SELECT id, email FROM patients"; // Also select email
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cmbPatientId.addItem(rs.getInt("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading patients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDoctorsIntoComboBox() {
        try {
            String query = "SELECT id, name FROM doctors"; // Select doctor ID and name
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cmbDoctorId.addItem(rs.getInt("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading doctors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addAppointment() {
        try {
            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            int patientId = (int) cmbPatientId.getSelectedItem();
            int doctorId = (int) cmbDoctorId.getSelectedItem();
            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setString(3, txtAppointmentDate.getText());
            pst.setString(4, txtAppointmentTime.getText());
            pst.setString(5, "Scheduled");
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment booked successfully!");

            // Get details for email before clearing text fields
            String doctorName = getDoctorName(doctorId);
            String patientEmail = getPatientEmail(patientId);
            sendAppointmentEmail(patientEmail, doctorName);

            clearInputFields(); // Now clear input fields after email is sent
            refreshAppointmentTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error booking appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getDoctorName(int doctorId) {
        try {
            String query = "SELECT name FROM doctors WHERE id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, doctorId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPatientEmail(int patientId) {
        try {
            String query = "SELECT email FROM patients WHERE id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void sendAppointmentEmail(String recipientEmail, String doctorName) {
        final String username = "sithijasulank789@gmail.com"; // Your email
        final String password = "qcom resh mjyp rhgv"; // Your App password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS security
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Appointment Confirmation");

            // Fetch date and time from the text fields
            String appointmentDate = txtAppointmentDate.getText(); // Date in YYYY-MM-DD format
            String appointmentTime = txtAppointmentTime.getText(); // Time in HH:MM:SS format

            // Ensure date and time are present in the message
            if (appointmentDate.isEmpty() || appointmentTime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Appointment Date or Time is missing!");
                return;  // Exit method if missing data
            }

            String emailContent = "Dear Patient,\n\n" +
                    "Your appointment has been scheduled with Dr. " + doctorName + ".\n" +
                    "Appointment Date: " + appointmentDate + "\n" +
                    "Appointment Time: " + appointmentTime + "\n\n" +
                    "Thank you for using our service.\n\nBest Regards,\nMedicare Team";

            // Set email message content
            message.setText(emailContent);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending email: " + e.getMessage());
        }
    }

    private void updateAppointment() {
        try {
            String query = "UPDATE appointments SET appointment_date=?, appointment_time=?, status=? WHERE appointment_id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtAppointmentDate.getText());
            pst.setString(2, txtAppointmentTime.getText());
            pst.setString(3, txtStatus.getText());
            pst.setInt(4, Integer.parseInt(txtAppointmentId.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment updated successfully!");
            clearInputFields();
            refreshAppointmentTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteAppointment() {
        try {
            String query = "DELETE FROM appointments WHERE appointment_id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtAppointmentId.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment deleted successfully!");
            clearInputFields();
            refreshAppointmentTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtAppointmentId.setText("");
        txtAppointmentDate.setText("");
        txtAppointmentTime.setText("");
        txtStatus.setText("");
    }

    private void refreshAppointmentTable() {
        try {
            String query = "SELECT * FROM appointments";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("status")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing appointment table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScheduleAppointments frame = new ScheduleAppointments();
            frame.setVisible(true); // Make sure the frame is visible
        });
    }
}
