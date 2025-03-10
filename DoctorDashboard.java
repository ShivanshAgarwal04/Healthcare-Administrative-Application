import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class DoctorDashboard extends JFrame {
    private JTextArea bookingsArea;
    private JTextArea messagesArea; 

    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(450, 500);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JLabel dateLabel = new JLabel("Booking Date (YYYY-MM-DD):");
        dateLabel.setBounds(10, 20, 200, 25);
        panel.add(dateLabel);

        JTextField dateField = new JTextField(20);
        dateField.setBounds(220, 20, 165, 25);
        panel.add(dateField);

        JLabel timeLabel = new JLabel("Booking Time (HH:MM):");
        timeLabel.setBounds(10, 50, 200, 25);
        panel.add(timeLabel);

        JTextField timeField = new JTextField(20);
        timeField.setBounds(220, 50, 165, 25);
        panel.add(timeField);

        JButton viewBookingsButton = new JButton("Find Booking");
        viewBookingsButton.setBounds(10, 80, 150, 25);
        panel.add(viewBookingsButton);

        bookingsArea = new JTextArea();
        bookingsArea.setBounds(10, 120, 400, 150);
        bookingsArea.setEditable(false);
        panel.add(bookingsArea);

        JLabel messagesLabel = new JLabel("Messages:");
        messagesLabel.setBounds(10, 280, 200, 25);
        panel.add(messagesLabel);

        messagesArea = new JTextArea();
        messagesArea.setBounds(10, 310, 400, 100);  
        messagesArea.setEditable(false);
        panel.add(messagesArea);

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateInput = dateField.getText();
                String timeInput = timeField.getText();

                String bookingDetails = getBookingDetails(dateInput, timeInput);
                bookingsArea.setText(bookingDetails);

            }
        });
    }
    private String getBookingDetails(String bookingDate, String bookingTime) {
        StringBuilder bookingInfo = new StringBuilder();
        String url = "jdbc:mysql://localhost:3306/doctorinterface";
        String user = "sagarwal";
        String password = "softwaredev";

        // Query to find a specific booking
        String query = "SELECT bookingNo, bookingDate, bookingTime, patientName, doctorName FROM bookings WHERE bookingDate = ? AND bookingTime = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, bookingDate);
            stmt.setString(2, bookingTime);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bookingInfo.append("Booking Found:\n")
                        .append("Booking No: ").append(rs.getInt("bookingNo")).append("\n")
                        .append("Date: ").append(rs.getDate("bookingDate")).append("\n")
                        .append("Time: ").append(rs.getTime("bookingTime")).append("\n")
                        .append("Patient Name: ").append(rs.getString("patientName")).append("\n")
                        .append("Doctor Name: ").append(rs.getString("doctorName")).append("\n");
            } else {
                bookingInfo.append("No booking found for the given date and time.");
            }

        } catch (SQLException e) {
            return "Error retrieving data: " + e.getMessage();
        }

        return bookingInfo.toString();
    }

    
    public static void main(String[] args) {
        new DoctorDashboard();
    }
}
