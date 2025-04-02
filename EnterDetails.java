import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Database Connection
class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Younm070.306";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static boolean confirmBookingID(Integer bookingNo){
        boolean isValid = false;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT bookingNo FROM Bookings WHERE bookingNo = ?")) {
            statement.setInt(1, bookingNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    isValid = (bookingNo.equals(resultSet.getInt("bookingNo")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
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

        enterVisitDetailsButton.addActionListener(e -> handleButtonClick(true));
        enterPrescriptionsButton.addActionListener(e -> handleButtonClick(false));
    }

    private void handleButtonClick(boolean isVisitDetails) {
        try {
            int bookingNo = Integer.parseInt(JOptionPane.showInputDialog("Enter booking number:"));
            if (isVisitDetails) {
                new EnterVisitDetails(bookingNo);
            } else {
                new EnterPrescriptions(bookingNo);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
        }
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
        if (!DBConnection.confirmBookingID(bookingNo)){
            JOptionPane.showMessageDialog(null, "Invalid booking number.");
            return;
        }
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

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO Visits (bookingNo, notes) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bookingNo);
            stmt.setString(2, notes);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int visitID = generatedKeys.getInt(1);
                    JOptionPane.showMessageDialog(this, "Visit details saved successfully! Visit ID: " + visitID);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save visit details.");
            }
            dispose();
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
        if (!DBConnection.confirmBookingID(bookingNo)){
            JOptionPane.showMessageDialog(null, "Invalid booking number.");
            return;
        }
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
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO Prescriptions (bookingNo, medicationName, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bookingNo);
            stmt.setString(2, medField.getText());
            stmt.setString(3, dosageField.getText());
            stmt.setString(4, durationField.getText());
            stmt.setString(5, instructionsField.getText());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int prescriptionID = generatedKeys.getInt(1);
                    JOptionPane.showMessageDialog(this, "Prescription saved successfully! Prescription ID: " + prescriptionID);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save prescription.");
            }
            dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription.");
        }
    }


}
