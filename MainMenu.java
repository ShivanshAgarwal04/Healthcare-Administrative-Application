import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;

public class MainMenu extends JFrame {
    private Integer doctorID;
    private String doctorName;
    LocalTime currentTime = LocalTime.now();
    private JPanel messagePanel;
    private ArrayList<JTextArea> messages;

    public MainMenu(Integer doctorID) {
        String doctorName = DBConnection.getDoctorName(doctorID);

        setTitle("Doctor Interface: Main Menu - " + doctorName);
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        messages = new ArrayList<>();
        placeComponents(panel2, gbc);
        add(panel2);

        setVisible(true);
    }

    private void placeComponents(JPanel panel2, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton viewBookings = new JButton("Bookings");
        viewBookings.setName("viewBookings");
        panel2.add(viewBookings, gbc);

        gbc.gridy++;
        JButton enterButton = new JButton("Enter Details");
        enterButton.setName("Enter details");
        panel2.add(enterButton, gbc);

        gbc.gridy++;
        JButton messageButton = new JButton("Messaging System");
        messageButton.setName("messageButton");
        panel2.add(messageButton, gbc);

        gbc.gridy++;
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setName("viewDetailsButton");
        panel2.add(viewDetailsButton, gbc);

        gbc.gridy++;
        JButton EditVisitDetailsButton = new JButton("Edit Visit Details");
        EditVisitDetailsButton.setName("Edit Visit Details");
        panel2.add(EditVisitDetailsButton, gbc);

        gbc.gridy++;
        JButton AssignNewDoctorButton = new JButton("Assign New Doctor");
        AssignNewDoctorButton.setName("Assign New Doctor");
        panel2.add(AssignNewDoctorButton, gbc);

        gbc.gridy++;
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        panel2.add(scrollPane, gbc);

        gbc.gridy++;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setName("logoutButton");
        panel2.add(logoutButton, gbc);

        // Action Listeners
        viewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorDashboard();
                System.out.println("Opened Bookings Dashboard for Dr. " + doctorName + " at " + currentTime);
            }
        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterDetails();
                System.out.println("Opened Enter Details screen at " + currentTime);
            }
        });

        messageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MessagingSystem(MainMenu.this);
            }
        });

        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewDetails();
                System.out.println("Opened View Details screen at " + currentTime);
            }
        });

        EditVisitDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter Booking ID to edit:");
                try {
                    int visitID = Integer.parseInt(input);
                    new EditVisitDetails(visitID);
                    System.out.println("Opened Edit Visit Details screen for Visit ID " + visitID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Visit ID entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        AssignNewDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter Booking Number to assign a new doctor:");
                try {
                    int bookingNo = Integer.parseInt(input);
                    new AssignNewDoctor(bookingNo);
                    System.out.println("Opened Assign New Doctor screen for Booking No: " + bookingNo + " at " + currentTime);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Booking Number entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginScreen();
                updateMessage("Logged out successfully");
            }
        });
    }

    public void updateMessage(String message) {
        JTextArea messageBox = new JTextArea(message);
        messageBox.setEditable(false);
        messageBox.setWrapStyleWord(true);
        messageBox.setLineWrap(true);
        messageBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messageBox.setBackground(new Color(240, 240, 240));
        messages.add(messageBox);
        messagePanel.add(messageBox);
        messagePanel.revalidate();
        messagePanel.repaint();
    }
}