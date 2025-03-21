import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Database Connection
class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/clinic";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Main window
public class EnterDetails extends JFrame {
    public EnterDetails() {
        setTitle("Enter visit details & prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(2, 1, 5, 5));

        JButton enterVisitDetailsButton = new JButton("Enter visit details");
        panel.add(enterVisitDetailsButton);

        JButton enterPrescriptionsButton = new JButton("Enter prescriptions");
        panel.add(enterPrescriptionsButton);

        enterVisitDetailsButton.addActionListener(e -> {
            int bookingNo = Integer.parseInt(JOptionPane.showInputDialog("Enter booking number:"));
            new EnterVisitDetails(bookingNo);
        });

        enterPrescriptionsButton.addActionListener(e -> {
            int bookingNo = Integer.parseInt(JOptionPane.showInputDialog("Enter booking number:"));
            new EnterPrescriptions(bookingNo);
        });
    }

    public static void main(String[] args) {
        new EnterDetails();
    }
}

// Enter Visit Details
class EnterVisitDetails extends JFrame {
    private int bookingNo;
    private JTextField notesField;

    public EnterVisitDetails(int bookingNo) {
        this.bookingNo = bookingNo;

        setTitle("Enter Visit Details");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(2, 2, 5, 5));

        panel.add(new JLabel("Enter any notes:"));
        notesField = new JTextField(10);
        panel.add(notesField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> saveVisitDetails());
        panel.add(new JLabel());
        panel.add(submitButton);
    }

    private void saveVisitDetails() {
        String notes = notesField.getText();

        try (Connection connection = DBConnection.getConnection()) {
            String query = "INSERT INTO Visits (bookingNo, notes) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookingNo);
            stmt.setString(2, notes);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Visit details saved successfully!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save visit details.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving visit details.");
        }
    }
}

// Enter Prescriptions
class EnterPrescriptions extends JFrame {
    private int bookingNo;
    private JTextField medField, dosageField, durationField, instructionsField;

    public EnterPrescriptions(int bookingNo) {
        this.bookingNo = bookingNo;

        setTitle("Enter Prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("Medication Name:"));
        medField = new JTextField(10);
        panel.add(medField);

        panel.add(new JLabel("Dosage:"));
        dosageField = new JTextField(10);
        panel.add(dosageField);

        panel.add(new JLabel("Duration:"));
        durationField = new JTextField(10);
        panel.add(durationField);

        panel.add(new JLabel("Instructions:"));
        instructionsField = new JTextField(10);
        panel.add(instructionsField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> savePrescription());
        panel.add(new JLabel());
        panel.add(submitButton);
    }

    private void savePrescription() {
        String medication = medField.getText();
        String dosage = dosageField.getText();
        String duration = durationField.getText();
        String instructions = instructionsField.getText();

        try (Connection connection = DBConnection.getConnection()) {
            String visitQuery = "SELECT visitID FROM Visits WHERE bookingNo = ?";
            PreparedStatement visitStmt = connection.prepareStatement(visitQuery);
            visitStmt.setInt(1, bookingNo);
            ResultSet resultSet = visitStmt.executeQuery();

            if (resultSet.next()) {
                int visitID = resultSet.getInt("visitID");

                String prescriptionQuery = "INSERT INTO Prescriptions (visitID, medicationName, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(prescriptionQuery);
                stmt.setInt(1, visitID);
                stmt.setString(2, medication);
                stmt.setString(3, dosage);
                stmt.setString(4, duration);
                stmt.setString(5, instructions);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Prescription saved successfully!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save prescription.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No matching visit found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription.");
        }
    }
}
