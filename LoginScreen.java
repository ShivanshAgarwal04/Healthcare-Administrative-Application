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
    private JButton loginButton;

    public LoginScreen() {
        setTitle("Doctors' System");
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
        usernameField.setName("usernameField"); // Set a unique name
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setName("passwordField"); // Set a unique name
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setName("loginButton"); // Set a unique name
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Login Successful!");
                    new DoctorDashboard();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid username or password.");
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String url = "jdbc:mysql://localhost:3306/doctorinterface";
        String dbUsername = "sagarwal";
       String dbPassword = "softwaredev";

        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");
    
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                System.out.println("Database connection successful.");
    
        
                String query = "SELECT password FROM doctorcredentials WHERE username = ?";
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.setString(1, username);
    
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String storedPassword = resultSet.getString("password");
                           
                            if (password.equals(storedPassword)) {
                                isValid = true;
                                System.out.println("Login successful.");
                            } else {
                                System.out.println("Passwords do not match.");
                            }
                        } else {
                            System.out.println("No user found with username: " + username);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. - blah");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    
        return isValid;
    }
    
    public static void main(String[] args) {
        new LoginScreen();
    }
}
