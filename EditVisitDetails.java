import javax.swing.*;
import java.awt.*;
import java.sql.*;

// Edit Visit Details
public class EditVisitDetails extends JFrame {
    private int bookingID;
    private int prescriptionID;
    private int visitID;
    JTextArea notesField;
    JTextField medicationField;
    JTextField dosageField;
    JTextField durationField;
    JTextField instructionsField;

    public EditVisitDetails(int bookingID) {
        this.bookingID = bookingID;

        setTitle("Edit Visit and Prescription Details");
        setSize(600, 400);  // Increased size for better visibility
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        // Load existing visit and prescription details
        loadExistingDetails();

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);  // Add some space around components
        constraints.anchor = GridBagConstraints.WEST;

        JLabel notesLabel = new JLabel("Edit Visit Notes:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(notesLabel, constraints);

        notesField = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(notesField);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        // Prescription fields (Medication, Dosage, Duration, Instructions)
        JLabel medicationLabel = new JLabel("Medication:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(medicationLabel, constraints);

        medicationField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(medicationField, constraints);

        JLabel dosageLabel = new JLabel("Dosage:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(dosageLabel, constraints);

        dosageField = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(dosageField, constraints);

        JLabel durationLabel = new JLabel("Duration:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(durationLabel, constraints);

        durationField = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(durationField, constraints);

        JLabel instructionsLabel = new JLabel("Instructions:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(instructionsLabel, constraints);

        instructionsField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(instructionsField, constraints);

        // Update button
        JButton submitButton = new JButton("Update");
        submitButton.addActionListener(e -> updateDetails());
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(submitButton, constraints);
    }

    // Loads the current visit details (notes) and prescription details from the database
    private void loadExistingDetails() {
        // First check if the bookingID exists in the Visits table
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT visitID FROM Visits WHERE bookingNo = ?")) {
            stmt.setInt(1, bookingID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    visitID = rs.getInt("visitID");

                    // Load the visit details using the visitID
                    loadVisitDetails();

                    // Load Prescription details using the same bookingID
                    loadPrescriptionDetails(bookingID);
                } else {
                    JOptionPane.showMessageDialog(this, "No visit found for the given Booking ID.");
                    dispose();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking booking ID.");
        }
    }

    // Loads the visit details (notes) from the Visits table
    private void loadVisitDetails() {
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
    }

    // Loads the current prescription details using bookingID
    private void loadPrescriptionDetails(int bookingID) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Prescriptions WHERE bookingNo = ?")) {
            stmt.setInt(1, bookingID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    prescriptionID = rs.getInt("prescriptionID");
                    medicationField.setText(rs.getString("medicationName"));
                    dosageField.setText(rs.getString("dosage"));
                    durationField.setText(rs.getString("duration"));
                    instructionsField.setText(rs.getString("instructions"));
                } else {
                    JOptionPane.showMessageDialog(this, "No prescription details found for the given Booking Number.");
                    dispose();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading prescription details.");
        }
    }

    // Updates both the visit details and prescription details in the database
    void updateDetails() {
        String notes = notesField.getText();
        String medication = medicationField.getText();
        String dosage = dosageField.getText();
        String duration = durationField.getText();
        String instructions = instructionsField.getText();

        // Checks if fields are empty
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

    // confirmation message to the doctor and patient
    void sendConfirmationMessage() {
        // Placeholder for sending confirmation logic
        JOptionPane.showMessageDialog(this, "Confirmation sent to doctor and patient.");
    }

}
