import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignNewDoctor extends JFrame {
    private int bookingNo;
    private int patientID;
    private String patientName;
    private JComboBox<String> doctorList;
    private List<Integer> doctorIDs;

    public AssignNewDoctor(int bookingNo) {
        this.bookingNo = bookingNo;
        doctorIDs = new ArrayList<>();

        setTitle("Assign New Doctor");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel patientLabel = new JLabel();
        add(patientLabel);

        loadPatientAndDoctorDetails(patientLabel);

        doctorList = new JComboBox<>();
        add(new JLabel("Select New Doctor:"));
        add(doctorList);
        loadDoctors();

        JButton assignButton = new JButton("Assign Doctor");
        assignButton.addActionListener(e -> assignDoctor());
        add(assignButton);

        setVisible(true);
    }

    private void loadPatientAndDoctorDetails(JLabel patientLabel) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT P.patientID, P.patientName, D.doctorID, D.doctorName " +
                             "FROM Bookings B " +
                             "JOIN Patients P ON B.patientID = P.patientID " +
                             "JOIN Doctors D ON B.doctorID = D.doctorID " +
                             "WHERE B.bookingNo = ?")) {

            stmt.setInt(1, bookingNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patientID = rs.getInt("patientID");
                    patientName = rs.getString("patientName");
                    String doctorName = rs.getString("doctorName");

                    patientLabel.setText("<html>Patient: <b>" + patientName + "</b><br>Current Doctor: <b>" + doctorName + "</b></html>");
                } else {
                    JOptionPane.showMessageDialog(this, "No patient found for this booking.");
                    dispose();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient and doctor details.");
            dispose();
        }
    }

    private void loadDoctors() {
        doctorList.removeAllItems();
        doctorIDs.clear();

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT doctorID, doctorName FROM Doctors")) {

            while (rs.next()) {
                doctorIDs.add(rs.getInt("doctorID"));
                doctorList.addItem(rs.getString("doctorName"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors.");
        }
    }

    private void assignDoctor() {
        int doctorIndex = doctorList.getSelectedIndex();
        if (doctorIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        int newDoctorID = doctorIDs.get(doctorIndex);
        try (Connection connection = DBConnection.getConnection()) {
            // Get old doctor details
            String oldDoctorName = "", oldDoctorEmail = "";
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT D.doctorName, D.email FROM Bookings B " +
                            "JOIN Doctors D ON B.doctorID = D.doctorID " +
                            "WHERE B.bookingNo = ?")) {
                stmt.setInt(1, bookingNo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        oldDoctorName = rs.getString("doctorName");
                        oldDoctorEmail = rs.getString("email");
                    }
                }
            }

            // Update the booking with the new doctor
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE Bookings SET doctorID = ? WHERE bookingNo = ?")) {
                stmt.setInt(1, newDoctorID);
                stmt.setInt(2, bookingNo);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor reassigned successfully.");
                    sendConfirmationMessage(connection, oldDoctorName, oldDoctorEmail, newDoctorID);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reassign doctor.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error during doctor reassignment.");
        }
    }

    private void sendConfirmationMessage(Connection connection, String oldDoctorName, String oldDoctorEmail, int newDoctorID) {
        String patientEmail = "", newDoctorName = "", newDoctorEmail = "";

        try {
            // Get patient email
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT email FROM Patients WHERE patientID = ?")) {
                stmt.setInt(1, patientID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientEmail = rs.getString("email");
                    }
                }
            }

            // Get new doctor info
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT doctorName, email FROM Doctors WHERE doctorID = ?")) {
                stmt.setInt(1, newDoctorID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        newDoctorName = rs.getString("doctorName");
                        newDoctorEmail = rs.getString("email");
                    }
                }
            }

            // Simulated confirmation messages
            JOptionPane.showMessageDialog(this, "Confirmation sent to:\n" +
                    "- Patient: " + patientName + " (" + patientEmail + ")\n" +
                    "- Old Doctor: " + oldDoctorName + " (" + oldDoctorEmail + ")\n" +
                    "- New Doctor: " + newDoctorName + " (" + newDoctorEmail + ")");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending confirmation message.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignNewDoctor(30001)); // Example Booking ID
    }
}