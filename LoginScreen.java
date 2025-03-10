import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Doctor Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        usernameField.setName("usernameField");  // Set the name for testing
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        passwordField.setName("passwordField");  // Set the name for testing
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.setName("loginButton");  // Set the name for testing
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Login Successful!");
                    new DoctorDashboard(); // this opens the doctor's dashboard
                    dispose(); // close and dispose of the login screen
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid username or password.");
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String url = "jdbc:mysql://localhost/doctorinterface?user=sagarwal&password=softwaredev";

        try (Connection conn = DriverManager.getConnection(url)) {
            // Query to retrieve the password for the given username
            String query = "SELECT password FROM doctorCredentials WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                if (password.equals(storedPassword)) {
                    isValid = true;  // Password matches
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
