import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class DoctorDashboard extends JFrame {
    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("Booking Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(15);
        panel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Booking Time (HH:MM:SS):"), gbc);
        gbc.gridx = 1;
        JTextField timeField = new JTextField(15);
        panel.add(timeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton viewBookingsButton = new JButton("Find Booking");
        panel.add(viewBookingsButton, gbc);

        gbc.gridy = 3;
        JTextArea bookingsArea = new JTextArea(10, 40);
        bookingsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingsArea);
        panel.add(scrollPane, gbc);

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateInput = dateField.getText().trim();
                String timeInput = timeField.getText().trim();

                if (!isValidDate(dateInput) || !isValidTime(timeInput)) {
                    bookingsArea.setText("Invalid date or time format. Use YYYY-MM-DD for date and HH:MM:SS for time.");
                    return;
                }

                String bookingDetails = getBookingDetails(dateInput, timeInput);
                bookingsArea.setText(bookingDetails);
            }
        });
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String getBookingDetails(String bookingDate, String bookingTime) {
        StringBuilder bookingInfo = new StringBuilder();
        String url = "jdbc:mysql://localhost:3306/doctorinterface";
        String user = "sagarwal";
        String password = "softwaredev";

        String query = "SELECT bookingNo, bookingDate, bookingTime, patientName, doctorName FROM bookings WHERE bookingDate = ? AND bookingTime = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, bookingDate);
            stmt.setString(2, bookingTime);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingInfo.append(String.format(
                        "Booking No: %d\nDate: %s\nTime: %s\nPatient Name: %s\nDoctor Name: %s\n\n",
                        rs.getInt("bookingNo"),
                        rs.getDate("bookingDate"),
                        rs.getTime("bookingTime"),
                        rs.getString("patientName"),
                        rs.getString("doctorName")
                ));
            }

            if (bookingInfo.length() == 0) {
                bookingInfo.append("No booking found for the given date and time.");
            }

        } catch (SQLException e) {
            return "Error retrieving data: " + e.getMessage();
        }

        return bookingInfo.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorDashboard::new);
    }
}
