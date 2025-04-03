import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewPatients extends JFrame {
    private int doctorID;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public ViewPatients(int doctorID) {
        this.doctorID = doctorID;
        setTitle("View Your Patients");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Column names for table
        String[] columnNames = {"Patient ID", "Name", "Phone Number", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(tableModel);


        loadPatientsData();


        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadPatientsData() {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT DISTINCT p.patientID, p.patientName, p.phoneNo, p.email " +
                             "FROM Patients p " +
                             "JOIN Bookings b ON p.patientID = b.patientID " +
                             "WHERE b.doctorID = ?")) {
            stmt.setInt(1, doctorID);

            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    Object[] row = {
                            rs.getInt("patientID"),
                            rs.getString("patientName"),
                            rs.getString("phoneNo"),
                            rs.getString("email")
                    };
                    tableModel.addRow(row);
                }

                if (!hasData) {
                    JOptionPane.showMessageDialog(this, "No patients found for this doctor.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient data.");
        }
    }

    public static void main(String[] args) {
        // Example: Open ViewPatients window for doctorID = 1
        new ViewPatients(1);
    }
}
