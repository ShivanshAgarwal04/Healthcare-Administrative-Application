import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Edit Visit Details
public class EditVisitDetails extends JFrame {
    private int bookingNo;
    private JTextArea notesField;

    public EditVisitDetails(int bookingNo) {
        this.bookingNo = bookingNo;

        setTitle("Edit Visit Details");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(2, 2, 5, 5));

        panel.add(new JLabel("Edit Visit Notes:"));
        notesField = new JTextArea(5, 20);
        panel.add(new JScrollPane(notesField));

        JButton submitButton = new JButton("Update");
        submitButton.addActionListener(e -> updateVisitDetails());
        panel.add(new JLabel());
        panel.add(submitButton);
    }

    private void updateVisitDetails() {
        String notes = notesField.getText();

        // Placeholder for updating visit details logic
        JOptionPane.showMessageDialog(this, "Visit details updated successfully!");
        sendConfirmationMessage();
        dispose();
    }

    private void sendConfirmationMessage() {
        // Placeholder for sending confirmation message
        JOptionPane.showMessageDialog(this, "Confirmation sent to doctor and patient.");
    }
}