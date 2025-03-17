import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorDashboard extends JFrame {
    public  JTextField dateField;
    private JTextArea bookingsArea;

    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel dateLabel = new JLabel("Enter Date (YYYY-MM):");
        dateLabel.setBounds(10, 20, 160, 25);
        panel.add(dateLabel);

        dateField = new JTextField(20);
        dateField.setBounds(180, 20, 165, 25);
        panel.add(dateField);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(10, 60, 150, 25);
        viewBookingsButton.setName("View Bookings");  // Set name for testing
        panel.add(viewBookingsButton);

        bookingsArea = new JTextArea();
        bookingsArea.setBounds(10, 100, 350, 150);
        bookingsArea.setName("bookingsArea");  // Set name for testing
        panel.add(bookingsArea);

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                viewBookings(date);
            }
        });
    }

    private void viewBookings(String date) {
        String url = "jdbc:mysql://localhost/doctorinterface?user=sagarwal&password=softwaredev";

        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT bookingDate, bookingTime, patientName, doctorName FROM bookings WHERE bookingDate = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, date);

            ResultSet resultSet2 = statement.executeQuery();

            if (resultSet2.next()) {
                String bookingDate = resultSet2.getString("bookingDate");
                String bookingTime = resultSet2.getString("bookingTime");
                String patientName = resultSet2.getString("patientName");
                String doctorName = resultSet2.getString("doctorName");


                bookingsArea.setText("Booking Found:\n" +
                        "Date: " + bookingDate + "\n" +
                        "Time: " + bookingTime + "\n" +
                        "Patient: " + patientName + "\n" +
                        "Doctor: " + doctorName);
            } else {
                bookingsArea.setText("No bookings found for the searched date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DoctorDashboard();
    }
}
