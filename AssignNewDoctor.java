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

        setTitle("Assign New Doctor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        doctorList = new JComboBox<>();
        patientList = new JComboBox<>();

        doctorIDs = new ArrayList<>();
        patientIDs = new ArrayList<>();

        add(new JLabel("Select New Doctor:"));
        add(doctorList);
        loadDoctors();

        add(new JLabel("Select Patient:"));
        add(patientList);

        doctorList.addActionListener(e -> loadPatients());

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

        int selectedDoctorIndex = doctorList.getSelectedIndex();
        if (selectedDoctorIndex == -1) return;

        int doctorID = doctorIDs.get(selectedDoctorIndex);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT DISTINCT P.patientID, P.patientName " +
                             "FROM Bookings B JOIN Patients P ON B.patientID = P.patientID " +
                             "WHERE B.doctorID = ?")) {

            stmt.setInt(1, doctorID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patientIDs.add(rs.getInt("patientID"));
                    patientList.addItem(rs.getString("patientName"));
                }
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

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE Bookings SET doctorID = ? WHERE bookingNo = ? AND patientID = ?")) {

            stmt.setInt(1, newDoctorID);
            stmt.setInt(2, bookingNo);
            stmt.setInt(3, patientID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor successfully assigned.");
                sendConfirmationMessage(newDoctorID, patientID);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update doctor assignment.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating doctor assignment.");
        }
    }

    private void sendConfirmationMessage(int newDoctorID, int patientID) {
        try (Connection connection = DBConnection.getConnection()) {
            String patientName = "", patientEmail = "";
            String newDoctorName = "", newDoctorEmail = "";
            String oldDoctorName = "", oldDoctorEmail = "";

            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT P.patientName, P.email, D.doctorName, D.email " +
                            "FROM Bookings B " +
                            "JOIN Patients P ON B.patientID = P.patientID " +
                            "JOIN Doctors D ON B.doctorID = D.doctorID " +
                            "WHERE B.bookingNo = ?")) {
                stmt.setInt(1, bookingNo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientName = rs.getString("patientName");
                        patientEmail = rs.getString("email");
                        oldDoctorName = rs.getString("doctorName");
                        oldDoctorEmail = rs.getString("email");
                    }
                }
            }

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

