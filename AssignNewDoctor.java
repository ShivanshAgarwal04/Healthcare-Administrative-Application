import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignNewDoctor extends JFrame {
    private int bookingNo;
    JComboBox<String> doctorList;
    List<Integer> doctorIDs; // Stores doctor IDs for reference

    // Constructor to initialize the JFrame
    public AssignNewDoctor(int bookingNo) {
        this.bookingNo = bookingNo;

        setTitle("Assign New Doctor");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        add(new JLabel("Select New Doctor:"));

        doctorList = new JComboBox<>();
        doctorIDs = new ArrayList<>();
        loadDoctors(); // Populate doctor dropdown

        add(doctorList);

        JButton assignButton = new JButton("Assign Doctor");
        assignButton.addActionListener(e -> assignDoctor());
        add(assignButton);

        setVisible(true);
    }

    // Load doctors from database into JComboBox
    private void loadDoctors() {
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT doctorID, doctorName FROM Doctors")) {

            while (rs.next()) {
                int doctorID = rs.getInt("doctorID");
                String doctorName = rs.getString("doctorName");

                doctorIDs.add(doctorID);
                doctorList.addItem (doctorName);

                doctorList.revalidate();
                doctorList.repaint();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors.");
        }
    }

    // Assign selected doctor to the booking
    void assignDoctor() {
        int selectedIndex = doctorList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        int newDoctorID = doctorIDs.get(selectedIndex);

        // Update the booking record in the database
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE Bookings SET doctorID = ? WHERE bookingNo = ?")) {
            stmt.setInt(1, newDoctorID);
            stmt.setInt(2, bookingNo);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor successfully assigned.");
                sendConfirmationMessage(newDoctorID);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update doctor assignment.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating doctor assignment.");
        }
    }

    // Send confirmation messages to the patient and both doctors
    private void sendConfirmationMessage(int newDoctorID) {
        try (Connection connection = DBConnection.getConnection()) {
            // Get patient and new doctor details
            String patientName = "", patientEmail = "";
            String newDoctorName = "", newDoctorEmail = "";
            String oldDoctorName = "", oldDoctorEmail = "";

            // Get patient info
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT P.patientName, P.email, D.doctorID, D.doctorName, D.email " +
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

            // Display messages (Replace this with actual email notifications if needed)
            JOptionPane.showMessageDialog(this, "Confirmation sent to:\n" +
                    "- Patient: " + patientName + " (" + patientEmail + ")\n" +
                    "- Old Doctor: " + oldDoctorName + " (" + oldDoctorEmail + ")\n" +
                    "- New Doctor: " + newDoctorName + " (" + newDoctorEmail + ")");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending confirmation message.");
        }
    }

    // Main method to run the JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignNewDoctor(1));
    }

}
