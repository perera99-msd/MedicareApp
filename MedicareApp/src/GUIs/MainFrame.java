package GUIs;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Set the title and size of the Main Frame
        setTitle("MEDICARE PLUS");
        setSize(1000, 700);
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up a background image
        setContentPane(new JLabel(new ImageIcon("path_to_your_image.jpg"))); // Use an actual image path here
        setLayout(new BorderLayout());

        // Add a Title Label "Medicare Plus" at the top
        JLabel titleLabel = new JLabel("MEDICARE PLUS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);  // White color for the title
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180));  // Background color for title
        titleLabel.setPreferredSize(new Dimension(getWidth(), 80));
        add(titleLabel, BorderLayout.NORTH); // Add to the top of the frame

        // Create a panel for buttons and add it to the frame (center or bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 10, 10));  // 3 rows, 2 columns for better layout
        buttonPanel.setOpaque(false);  // Make the panel transparent

        // Add buttons to the button panel
        JButton btnManagePatients = createButton("Manage Patients");
        JButton btnManageDoctors = createButton("Manage Doctors");
        JButton btnManageSchedules = createButton("Manage Doctor Schedule");
        JButton btnManageInventory = createButton("Manage Inventory");
        JButton btnBookAppointments = createButton("Book Appointment");
        JButton btnGenerateReport = createButton("Generate Monthly Report");

        // Add action listeners to buttons
        btnManagePatients.addActionListener(e -> openManagePatients());
        btnManageDoctors.addActionListener(e -> openManageDoctors());
        btnManageSchedules.addActionListener(e -> openManageSchedules());
        btnManageInventory.addActionListener(e -> openManageInventory());
        btnBookAppointments.addActionListener(e -> openBookAppointments());
        btnGenerateReport.addActionListener(e -> openGenerateReport());

        // Add buttons to button panel
        buttonPanel.add(btnManagePatients);
        buttonPanel.add(btnManageDoctors);
        buttonPanel.add(btnManageSchedules);
        buttonPanel.add(btnManageInventory);
        buttonPanel.add(btnBookAppointments);
        buttonPanel.add(btnGenerateReport);

        // Add the button panel to the center of the main frame
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);  // Make the window visible
    }

    // Helper method to create buttons with a uniform style
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));  // Blue color
        button.setFocusPainted(false);
        return button;
    }

    // Open Manage Patients GUI
    private void openManagePatients() {
        new PatientManagement().setVisible(true);
    }

    // Open Manage Doctors GUI
    private void openManageDoctors() {
        new DoctorManagement().setVisible(true);
    }

    // Open Manage Doctor Schedule GUI
    private void openManageSchedules() {
        new ScheduleDoctors().setVisible(true);
    }

    // Open Manage Inventory GUI
    private void openManageInventory() {
        new ManagePharmacyInventory().setVisible(true);
    }

    // Open Book Appointment GUI
    private void openBookAppointments() {
        new ScheduleAppointments().setVisible(true);
    }

    // Open Generate Report GUI
    private void openGenerateReport() {
        new MonthlyReports().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
