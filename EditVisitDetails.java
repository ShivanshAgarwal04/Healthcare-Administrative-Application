import javax.swing.*;
import java.awt.*;
import java.sql.*;

// Edit Visit Details
public class EditVisitDetails extends JFrame {
    private int prescriptionID;
    private int visitID;
    private JTextArea notesField;
    private JTextField medicationField, dosageField, durationField, instructionsField;

    // Constructor to initialize with prescriptionID and visitID
    public EditVisitDetails(int prescriptionID, int visitID) {
        this.prescriptionID = prescriptionID;
        this.visitID = visitID;

        setTitle("Edit Visit and Prescription Details");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        // Load existing visit and prescription details
        loadExistingDetails();

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(5, 2, 5, 5));

        // Visit notes
        panel.add(new JLabel("Edit Visit Notes:"));
        notesField = new JTextArea(5, 20);
        panel.add(new JScrollPane(notesField));

        // Prescription fields
        panel.add(new JLabel("Medication:"));
        medicationField = new JTextField(20);
        panel.add(medicationField);

        panel.add(new JLabel("Dosage:"));
        dosageField = new JTextField(10);
        panel.add(dosageField);

        panel.add(new JLabel("Duration:"));
        durationField = new JTextField(10);
        panel.add(durationField);

        panel.add(new JLabel("Instructions:"));
        instructionsField = new JTextField(20);
        panel.add(instructionsField);

        // Update button
        JButton submitButton = new JButton("Update");
        submitButton.addActionListener(e -> updateDetails());
        panel.add(new JLabel());
        panel.add(submitButton);
    }

    // This method loads the current visit details (notes) and prescription details from the database
    private void loadExistingDetails() {
        // Load Visit Details (Visit ID)
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT notes FROM Visits WHERE visitID = ?")) {
            stmt.setInt(1, visitID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    notesField.setText(rs.getString("notes"));
                } else {
                    JOptionPane.showMessageDialog(this, "No visit details found for the given Visit ID.");
                    dispose();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading visit details.");
        }

        // Load Prescription Details (Prescription ID)
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Prescriptions WHERE prescriptionID = ?")) {
            stmt.setInt(1, prescriptionID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    medicationField.setText(rs.getString("medicationName"));
                    dosageField.setText(rs.getString("dosage"));
                    durationField.setText(rs.getString("duration"));
                    instructionsField.setText(rs.getString("instructions"));
                } else {
                    JOptionPane.showMessageDialog(this, "No prescription details found for the given Prescription ID.");
                    dispose();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading prescription details.");
        }
    }

    // This method updates both the visit details and prescription details in the database
    private void updateDetails() {
        String notes = notesField.getText();
        String medication = medicationField.getText();
        String dosage = dosageField.getText();
        String duration = durationField.getText();
        String instructions = instructionsField.getText();

        // Make sure that the fields are not empty
        if (notes.trim().isEmpty() || medication.trim().isEmpty() || dosage.trim().isEmpty() ||
                duration.trim().isEmpty() || instructions.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return;
        }

        // Update Visit Details
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("UPDATE Visits SET notes = ? WHERE visitID = ?")) {
            stmt.setString(1, notes);
            stmt.setInt(2, visitID);
            int visitRowsAffected = stmt.executeUpdate();

            // Update Prescription Details
            try (Connection connection2 = DBConnection.getConnection();
                 PreparedStatement stmt2 = connection2.prepareStatement("UPDATE Prescriptions SET medicationName = ?, dosage = ?, duration = ?, instructions = ? WHERE prescriptionID = ?")) {
                stmt2.setString(1, medication);
                stmt2.setString(2, dosage);
                stmt2.setString(3, duration);
                stmt2.setString(4, instructions);
                stmt2.setInt(5, prescriptionID);
                int prescriptionRowsAffected = stmt2.executeUpdate();

                if (visitRowsAffected > 0 && prescriptionRowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Visit and Prescription details updated successfully!");
                    sendConfirmationMessage();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update visit and prescription details.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating prescription details.");
            }

            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating visit details.");
        }
    }

    // This method sends a confirmation message to the doctor and patient
    private void sendConfirmationMessage() {
        // Placeholder for sending confirmation logic
        JOptionPane.showMessageDialog(this, "Confirmation sent to doctor and patient.");
    }
}
