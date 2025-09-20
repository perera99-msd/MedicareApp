package GUIs;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Properties;

public class ManagePharmacyInventory extends JFrame {
    private Connection connection;
    private JTextField txtMedicineName, txtQuantity, txtPrice, txtSearchMedicineId;
    private JTable inventoryTable;

    public ManagePharmacyInventory() {
        // Initialize database connection
        initializeDatabaseConnection();

        // Initialize components
        initializeComponents();

        // Refresh inventory table to display current data
        refreshInventoryTable();
    }

    private void initializeComponents() {
        // Set up frame
        setTitle("Pharmacy Inventory Management");
        setSize(750, 600);  // Optimized frame size
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set frame background color
        getContentPane().setBackground(new Color(250, 250, 250));

        // Create tabbed pane for multiple panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Add/Update Inventory
        JPanel addUpdateInventoryPanel = createAddUpdateInventoryPanel();

        // Panel for displaying Inventory List
        JPanel inventoryListPanel = createInventoryListPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Add / Update Inventory", addUpdateInventoryPanel);
        tabbedPane.addTab("Inventory List", inventoryListPanel);

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
            // Close current frame (ManagePharmacyInventory)
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

    private JPanel createAddUpdateInventoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 245));

        inputPanel.add(createLabel("Medicine Name:"));
        txtMedicineName = createTextField();
        inputPanel.add(txtMedicineName);

        inputPanel.add(createLabel("Quantity:"));
        txtQuantity = createTextField();
        inputPanel.add(txtQuantity);

        inputPanel.add(createLabel("Price:"));
        txtPrice = createTextField();
        inputPanel.add(txtPrice);

        // Button Panel for Add, Update, Delete, Clear
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton(buttonPanel, "Add Item", e -> addItem());
        addButton(buttonPanel, "Update Item", e -> updateItem());
        addButton(buttonPanel, "Delete Item", e -> deleteItem());
        addButton(buttonPanel, "Clear", e -> clearInputFields());

        // Add input fields panel and buttons to the main panel
        panel.add(inputPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createInventoryListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Inventory search field and button
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchPanel.add(createLabel("Search by Medicine ID:"));
        txtSearchMedicineId = new JTextField(12);
        searchPanel.add(txtSearchMedicineId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(85, 133, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchItem());
        searchPanel.add(btnSearch);

        // Inventory table setup
        inventoryTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Medicine Name", "Quantity", "Price"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);

        // Add search panel and table to the inventory list panel
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
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to send an email when stock is low
    private void sendLowStockEmail(String medicineName, int quantity) {
        String host = "smtp.gmail.com";  // SMTP Server for Gmail
        String port = "587"; // TLS port for Gmail SMTP
        final String username = "sithijasulank789@gmail.com";  // Your email
        final String password = "qcom resh mjyp rhgv";  // App password

        // Email settings
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Session setup
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("receptionist@example.com"));
            message.setSubject("Low Stock Alert: " + medicineName);
            message.setText("The quantity of " + medicineName + " has dropped below 10. Current stock: " + quantity);

            // Send the email
            Transport.send(message);
            System.out.println("Low stock email sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void addItem() {
        try {
            String query = "INSERT INTO pharmacy_inventory (medicine_name, quantity, price) VALUES (?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtMedicineName.getText());
            pst.setInt(2, Integer.parseInt(txtQuantity.getText()));
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.executeUpdate();

            // Check if quantity is below 10, then send an email
            int quantity = Integer.parseInt(txtQuantity.getText());
            if (quantity < 10) {
                sendLowStockEmail(txtMedicineName.getText(), quantity);
            }

            JOptionPane.showMessageDialog(null, "Item added successfully!");
            clearInputFields();
            refreshInventoryTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateItem() {
        try {
            String query = "UPDATE pharmacy_inventory SET medicine_name=?, quantity=?, price=? WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, txtMedicineName.getText());
            pst.setInt(2, Integer.parseInt(txtQuantity.getText()));
            pst.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pst.setInt(4, Integer.parseInt(txtSearchMedicineId.getText())); // Assuming the ID is being searched
            pst.executeUpdate();

            // Check if quantity is below 10, then send an email
            int quantity = Integer.parseInt(txtQuantity.getText());
            if (quantity < 10) {
                sendLowStockEmail(txtMedicineName.getText(), quantity);
            }

            JOptionPane.showMessageDialog(null, "Item updated successfully!");
            clearInputFields();
            refreshInventoryTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteItem() {
        try {
            String query = "DELETE FROM pharmacy_inventory WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtSearchMedicineId.getText())); // Use search ID for deletion
            pst.executeUpdate();

            // Refresh the inventory table after deletion
            refreshInventoryTable();

            JOptionPane.showMessageDialog(null, "Item deleted successfully!");
            clearInputFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchItem() {
        try {
            String query = "SELECT * FROM pharmacy_inventory WHERE id=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtSearchMedicineId.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtMedicineName.setText(rs.getString("medicine_name"));
                txtQuantity.setText(rs.getString("quantity"));
                txtPrice.setText(rs.getString("price"));
            } else {
                JOptionPane.showMessageDialog(null, "Item not found!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtMedicineName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtSearchMedicineId.setText("");
    }

    private void refreshInventoryTable() {
        try {
            String query = "SELECT * FROM pharmacy_inventory";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String medicineName = rs.getString("medicine_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                // Add row to the table
                Object[] row = {id, medicineName, quantity, price};
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing inventory table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ManagePharmacyInventory frame = new ManagePharmacyInventory();
                frame.setVisible(true);
            }
        });
    }
}
