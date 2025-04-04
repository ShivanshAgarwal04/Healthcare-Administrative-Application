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
        panel.add(viewBookingsButton);

        bookingsArea = new JTextArea();
        bookingsArea.setBounds(10, 100, 350, 150);
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
        // 1. First verify the doctorName value
        System.out.println("[DEBUG] Current doctorName: '" + this.doctorName + "'");
        System.out.println("[DEBUG] Date entered: " + date);

        // 2. Validate date format (YYYY-MM)
        if (date == null || !date.matches("\\d{4}-\\d{2}")) {
            bookingsArea.setText("Invalid format. Use YYYY-MM");
            return;
        }
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);

        System.out.println("Searching for bookings for Doctor: " + this.doctorName); // Debugging
        System.out.println("Year: " + year + ", Month: " + month); // Debugging


        try (Connection conn = DBConnection.getConnection()) {
            // Query to match doctorName correctly using a JOIN
            String query = "SELECT b.dayOfBooking, b.monthOfBooking, b.yearOfBooking, b.bookingTime, b.patientID " +
                    "FROM bookings b " +
                    "JOIN doctors d ON b.doctorID = d.doctorID " +
                    "WHERE b.yearOfBooking = ? AND b.monthOfBooking = ? AND d.doctorName = ?";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, year);
            statement.setString(2, month);
            statement.setString(3, doctorName);

            System.out.println("Executing SQL Query: " + statement.toString()); // Debugging

            ResultSet resultSet = statement.executeQuery();

            StringBuilder bookingsText = new StringBuilder();
            boolean found = false;

            while (resultSet.next()) {
                found = true;
                String dayOfBooking = resultSet.getString("dayOfBooking");
                String monthOfBooking = resultSet.getString("monthOfBooking");
                String yearOfBooking = resultSet.getString("yearOfBooking");
                String bookingTime = resultSet.getString("bookingTime");
                String patientID = resultSet.getString("patientID");

                String calendarDate = dayOfBooking + "-" + monthOfBooking + "-" + yearOfBooking;

                bookingsText.append("Date: ").append(calendarDate)
                        .append("\nTime: ").append(bookingTime)
                        .append("\nPatient: ").append(patientID)
                        .append("\n------------------\n");
            }

            if (found) {
                bookingsArea.setText(bookingsText.toString());
            } else {
                bookingsArea.setText("No bookings found for " + doctorName + " in " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            bookingsArea.setText("Error fetching bookings: " + e.getMessage());
        } catch (StringIndexOutOfBoundsException e) {
            bookingsArea.setText("Please enter date in YYYY-MM format");
        }
    }


}
