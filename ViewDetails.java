import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDetails extends JFrame {
    public ViewDetails() {
        setTitle("View visit details & prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(2, 1, 5, 5));

        JButton viewVisitDetailsButton = new JButton("View visit details");
        panel.add(viewVisitDetailsButton);

        JButton viewPrescriptionsButton = new JButton("View prescriptions");
        panel.add(viewPrescriptionsButton);

        viewVisitDetailsButton.addActionListener(e -> confirmVisitID());
        viewPrescriptionsButton.addActionListener(e -> confirmPrescriptionID());
    }

    private void confirmPrescriptionID() {
        try {
            int prescriptionID = Integer.parseInt(JOptionPane.showInputDialog("Enter Booking ID:"));
            new ViewPrescription(prescriptionID);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
        }
    }
    private void confirmVisitID() {
        try {
            int visitID = Integer.parseInt(JOptionPane.showInputDialog("Enter VisitID number:"));
            new ViewVisitDetails(visitID);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
        }
    }

    public static void main(String[] args) {
        new ViewDetails();
    }
}
class ViewVisitDetails extends JFrame {
    public ViewVisitDetails(int visitID) {
        setTitle("Visit Details");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Visit ID", "Booking No", "Notes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Visits WHERE visitID = ?")) {
            stmt.setInt(1, visitID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getInt("visitID"), rs.getInt("bookingNo"), rs.getString("notes")});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving visit details.");
        }

        setVisible(true);
    }
}

class ViewPrescription extends JFrame {
    public ViewPrescription(int bookingNo) {
        setTitle("Prescription Details");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Prescription ID", "Booking No", "Medication", "Dosage", "Duration", "Instructions"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT p.prescriptionID, v.bookingNo, p.medicationName, p.dosage, p.duration, p.instructions " +
                "FROM Prescriptions p " +
                "JOIN Visits v ON p.visitID = v.visitID " +
                "WHERE v.bookingNo = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingNo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("prescriptionID"),
                            rs.getInt("bookingNo"),
                            rs.getString("medicationName"),
                            rs.getString("dosage"),
                            rs.getString("duration"),
                            rs.getString("instructions")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving prescription details.");
        }

        setVisible(true);
    }
}



