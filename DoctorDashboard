import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorDashboard extends JFrame {
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

        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setBounds(10, 20, 80, 25);
        panel.add(monthLabel);

        JTextField monthField = new JTextField(20);
        monthField.setBounds(100, 20, 165, 25);
        panel.add(monthField);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setBounds(10, 50, 80, 25);
        panel.add(yearLabel);

        JTextField yearField = new JTextField(20);
        yearField.setBounds(100, 50, 165, 25);
        panel.add(yearField);

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setBounds(10, 80, 120, 25);
        panel.add(viewBookingsButton);

        JTextArea bookingsArea = new JTextArea();
        bookingsArea.setBounds(10, 110, 350, 130);
        panel.add(bookingsArea);

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
                String bookings = getBookings(month, year);
                bookingsArea.setText(bookings);
            }
        });
    }
    // for this one aswell i have return a staticly until someone does the database
    private String getBookings(int month, int year) {

        return "Booking 1: 2023-10-15 10:00 AM\nBooking 2: 2023-10-20 02:00 PM";
    }


}