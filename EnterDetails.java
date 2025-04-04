import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;




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
        if (visitDetailsExist()){
            // Directs to edit details
            new EditVisitDetails(bookingNo);
            dispose();
        }
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
    private boolean visitDetailsExist() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT 1 FROM Visits WHERE bookingNo = ?")) {
            stmt.setInt(1, bookingNo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a record is found, return true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

// Enter Prescriptions
// Enter Prescriptions
class EnterPrescriptions extends JFrame {
    private int visitID;
    private JTextField medField, dosageField, durationField, instructionsField;
    private Integer bookingNo;

    public EnterPrescriptions(Integer bookingNo) {
        // Confirm the booking number first
        this.bookingNo = bookingNo;
        if (!DBConnection.confirmBookingID(bookingNo)){
            JOptionPane.showMessageDialog(null, "Invalid booking number.");
            return;
        }

        // Retrieve the corresponding visit ID for the given booking
        this.visitID = getVisitIDForBooking(bookingNo);

        setTitle("Enter Prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private int getVisitIDForBooking(int bookingNo) {
        int visitID = -1; // default invalid value
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT visitID FROM Visits WHERE bookingNo = ?")) {
            stmt.setInt(1, bookingNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    visitID = rs.getInt("visitID");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return visitID;
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
        if (prescriptionExists()){
            //redirects to edit visit details and prescriptions
            new EditVisitDetails(bookingNo);
            dispose();
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO Prescriptions (visitID, medicationName, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, visitID); // Use visitID from the constructor
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
    private boolean prescriptionExists() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT 1 FROM Prescriptions WHERE visitID = ?")) {
            stmt.setInt(1, visitID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a prescription record is found, return true
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}

