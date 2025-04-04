import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagingSystem extends JFrame{
    JTextField recipientField;
    JTextField patientField;
    JTextArea messageBody;
    JButton sendButton;
    private MainMenu mainMenu;

    public MessagingSystem(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Messaging System");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Recipient field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Recipient:"), gbc);

        gbc.gridx = 1;
        recipientField = new JTextField(20);
        panel.add(recipientField, gbc);

        // Patient field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Patient (Optional):"), gbc);

        gbc.gridx = 1;
        patientField = new JTextField(20);
        panel.add(patientField, gbc);

        // Message body
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Message:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        messageBody = new JTextArea(5, 20);
        panel.add(new JScrollPane(messageBody), gbc);

        // Send button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        sendButton = new JButton("Send Message");
        panel.add(sendButton, gbc);

        // Send button action
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        add(panel);
        setVisible(true);
    }

    void sendMessage() {
        String recipient = recipientField.getText().trim();
        String patient = patientField.getText().trim();
        String message = messageBody.getText().trim();

        if (recipient.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Recipient and Message cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fullMessage = "To: " + recipient + " | " + message;
        if (!patient.isEmpty()) {
            fullMessage += " (Regarding Patient: " + patient + ")";
        }

        mainMenu.updateMessage(fullMessage);
        JOptionPane.showMessageDialog(this, "Message sent successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}

