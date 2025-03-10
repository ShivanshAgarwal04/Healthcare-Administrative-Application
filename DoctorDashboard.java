import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DoctorDashboard extends JFrame {

    private JTextField dateField;
    private JTextArea bookingsArea;
    private JButton viewBookingsButton;
    private Map<String, String> bookings;

    public DoctorDashboard() {

        setTitle("Doctor Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setLayout(new FlowLayout());


        dateField = new JTextField(10);
        dateField.setName("dateField");
        add(dateField);

        // View bookings button
        viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setName("View Bookings");
        viewBookingsButton.addActionListener(new ViewBookingsButtonListener());
        add(viewBookingsButton);

        bookingsArea = new JTextArea(10, 30);
        bookingsArea.setName("bookingsArea");
        bookingsArea.setEditable(false);
        add(new JScrollPane(bookingsArea));

        bookings = new HashMap<>();
        bookings.put("2025-03-10", "Booking Found:\nDate: 2025-03-10\nTime: 15:00:00\n");
    }


    private class ViewBookingsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredDate = dateField.getText();
            String bookingInfo = bookings.get(enteredDate);

            if (bookingInfo != null) {
                bookingsArea.setText(bookingInfo);
            } else {
                bookingsArea.setText("");
            }
        }
    }

    // Main method for testing
    public static void main(String[] args) {
       new DoctorDashboard();
    }
}
