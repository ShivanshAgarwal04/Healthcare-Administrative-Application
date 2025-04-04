import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorDashboard extends JFrame {
    public JTextField dateField;
    private JTextArea bookingsArea;
    private String doctorName;
    private Integer doctorID;

    public DoctorDashboard(String doctorName, Integer doctorID) {
        this.doctorName = doctorName;
        this.doctorID = doctorID;

        setTitle("Doctor Dashboard - " + doctorName);
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JLabel dateLabel = new JLabel("Enter Date (YYYY-MM):");
        dateLabel.setBounds(10, 20, 160, 25);
        panel.add(dateLabel);

        dateField = new JTextField(20);
        dateField.setBounds(180, 20, 165, 25);
        panel.add(dateField);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(10, 60, 150, 25);
        panel.add(viewBookingsButton);

        bookingsArea = new JTextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setLineWrap(true);
        bookingsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(bookingsArea);
        scrollPane.setBounds(10, 100, 410, 190);
        panel.add(scrollPane);

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText().trim();
                viewBookings(date);
            }
        });
    }

    private void viewBookings(String date) {
        System.out.println("[DEBUG] Current doctorName: '" + this.doctorName + "'");
        System.out.println("[DEBUG] Date entered: " + date);

        if (date == null || !date.matches("\\d{4}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid format. Use YYYY-MM", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String year = date.substring(0, 4);
        String month = date.substring(5, 7);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT b.dayOfBooking, b.monthOfBooking, b.yearOfBooking, b.bookingTime, " +
                    "p.patientName " +
                    "FROM bookings b " +
                    "JOIN doctors d ON b.doctorID = d.doctorID " +
                    "JOIN patients p ON b.patientID = p.patientID " +
                    "WHERE b.yearOfBooking = ? AND b.monthOfBooking = ? AND d.doctorName = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, year);
            statement.setString(2, month);
            statement.setString(3, doctorName);

            System.out.println("Executing SQL Query: " + statement); // Debugging

            ResultSet resultSet = statement.executeQuery();

            StringBuilder bookingsText = new StringBuilder();
            boolean found = false;

            while (resultSet.next()) {
                found = true;
                String day = resultSet.getString("dayOfBooking");
                String time = resultSet.getString("bookingTime");
                String patientName = resultSet.getString("patientName");

                bookingsText.append("📅 Date: ").append(String.format("%s-%s-%s", day, month, year))
                        .append("\n⏰ Time: ").append(time)
                        .append("\n👤 Patient: ").append(patientName)
                        .append("\n------------------------------\n");
            }

            if (found) {
                bookingsArea.setText(bookingsText.toString());
            } else {
                bookingsArea.setText("No bookings found for " + doctorName + " in " + date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            bookingsArea.setText("Error fetching bookings: " + e.getMessage());
        }
    }
}
