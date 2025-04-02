import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Doctor Interface: Main Menu");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel2 = new JPanel();
        panel2.setLayout(null); // Absolute positioning
        add(panel2);

        placeComponents(panel2);

        setVisible(true); // Ensure visibility after components are added
    }

    private void placeComponents(JPanel panel2) {
        JButton viewBookings = new JButton("Bookings");
        viewBookings.setBounds(10, 40, 150, 25);
        viewBookings.setName("viewBookings");
        panel2.add(viewBookings);

        JButton enterButton = new JButton("Enter Details");
        enterButton.setBounds(10, 80, 150, 25); // Position below "Bookings"
        enterButton.setName("Enter details");
        panel2.add(enterButton);

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBounds(10, 120, 150, 25);
        viewDetailsButton.setName("View details");
        panel2.add(viewDetailsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 160, 150, 25); // Adjusted position
        logoutButton.setName("logoutButton");
        panel2.add(logoutButton);

        // Add action listeners
        viewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorDashboard();
            }
        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterDetails(); // Open EnterDetails when clicked
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginScreen();
            }
        });

        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewDetails();
            }
        });
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
