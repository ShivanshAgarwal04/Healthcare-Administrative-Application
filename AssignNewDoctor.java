import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignNewDoctor extends JFrame {
    private int bookingNo;
    JComboBox<String> doctorList;
    JComboBox<String> patientList;
    List<Integer> doctorIDs;
    List<Integer> patientIDs;

    public AssignNewDoctor(int bookingNo) {
        this.bookingNo = bookingNo;

        setTitle("Assign New Doctor to Patient");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        doctorList = new JComboBox<>();
        patientList = new JComboBox<>();

        doctorIDs = new ArrayList<>();
        patientIDs = new ArrayList<>();

        add(new JLabel("Select Patient:"));
        add(patientList);
        loadPatients();

        add(new JLabel("Select New Doctor:"));
        add(doctorList);
        loadDoctors();

        JButton assignButton = new JButton("Assign Doctor");
        assignButton.addActionListener(e -> assignDoctor());
        add(assignButton);

        setVisible(true);
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

    private void loadPatients() {
        patientList.removeAllItems();
        patientIDs.clear();

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT patientID, patientName FROM Patients")) {

            while (rs.next()) {
                patientIDs.add(rs.getInt("patientID"));
                patientList.addItem(rs.getString("patientName"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients.");
        }
    }

    private void assignDoctor() {
        int doctorIndex = doctorList.getSelectedIndex();
        int patientIndex = patientList.getSelectedIndex();

        if (doctorIndex == -1 || patientIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select both a doctor and a patient.");
            return;
        }

        int newDoctorID = doctorIDs.get(doctorIndex);
        int patientID = patientIDs.get(patientIndex);
        int foundBookingNo = -1;

        try (Connection connection = DBConnection.getConnection()) {
            // Get the current booking number for this patient
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT bookingNo FROM Bookings WHERE patientID = ?")) {
                stmt.setInt(1, patientID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        foundBookingNo = rs.getInt("bookingNo");
                    } else {
                        JOptionPane.showMessageDialog(this, "No booking found for this patient.");
                        return;
                    }
                }
            }

            // Update the doctor in the booking
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE Bookings SET doctorID = ? WHERE bookingNo = ?")) {
                stmt.setInt(1, newDoctorID);
                stmt.setInt(2, foundBookingNo);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor reassigned successfully.");
                    sendConfirmationMessage(connection, foundBookingNo, newDoctorID);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reassign doctor.");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error during doctor reassignment.");
        }
    }

    private void sendConfirmationMessage(Connection connection, int bookingNo, int newDoctorID) {
        String patientName = "", patientEmail = "";
        String newDoctorName = "", newDoctorEmail = "";
        String oldDoctorName = "", oldDoctorEmail = "";

        try {
            // Get patient and old doctor info
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT P.patientName, P.email AS patientEmail, D.doctorName AS oldDocName, D.email AS oldDocEmail " +
                            "FROM Bookings B " +
                            "JOIN Patients P ON B.patientID = P.patientID " +
                            "JOIN Doctors D ON B.doctorID = D.doctorID " +
                            "WHERE B.bookingNo = ?")) {
                stmt.setInt(1, bookingNo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientName = rs.getString("patientName");
                        patientEmail = rs.getString("patientEmail");
                        oldDoctorName = rs.getString("oldDocName");
                        oldDoctorEmail = rs.getString("oldDocEmail");
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

            // Simulate sending messages
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
        SwingUtilities.invokeLater(() -> new AssignNewDoctor(1));
    }
}
