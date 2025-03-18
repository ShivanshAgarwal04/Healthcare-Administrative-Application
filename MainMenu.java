import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Doctor Interface: Main Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel2 = new JPanel();
        panel2.setLayout(null); // Ensure absolute positioning
        add(panel2);

        placeComponents(panel2);

        setVisible(true); // Ensure visibility after components are added
    }

    private void placeComponents(JPanel panel2) {
        JButton viewBookings = new JButton("Bookings");
        viewBookings.setBounds(10, 40, 250, 25);
        viewBookings.setName("viewBookings");
        panel2.add(viewBookings);

        JButton enterDetailsButton = new JButton("Enter visit details and prescriptions");
        enterDetailsButton.setBounds(10,60,250,25);
        enterDetailsButton.setName("Enter details");
        panel2.add(enterDetailsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 80, 250, 25);
        logoutButton.setName("logoutButton");
        panel2.add(logoutButton);

        viewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorDashboard();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginScreen();
            }
        });

        enterDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterDetails();
            }
        });
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
