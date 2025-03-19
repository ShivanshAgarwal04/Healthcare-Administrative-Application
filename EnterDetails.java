import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterDetails extends JFrame
{
    public EnterDetails() {
        setTitle("Enter visit details & prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }
    private void placeComponents(JPanel panel) {
        JButton enterPrescriptionsButton = new JButton("Enter prescriptions");
        enterPrescriptionsButton.setBounds(10, 60, 150, 25);
        enterPrescriptionsButton.setName("Enter prescriptions ");
        panel.add(enterPrescriptionsButton);

        JButton enterVisitDetailsButton = new JButton("Enter visit details");
        enterVisitDetailsButton.setBounds(10, 60, 150, 25);
        enterVisitDetailsButton.setName("Enter Visit details ");
        panel.add(enterVisitDetailsButton);

        enterVisitDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterVisitDetails();
            }
        });
        enterPrescriptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EnterPrescriptions();
            }
        });
    }
}

class EnterPrescriptions extends JFrame {
    public EnterPrescriptions() {
        setTitle("Enter prescriptions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel medicationName = new JLabel("Enter medication name:");
        panel.add(medicationName);
        JTextField field = new JTextField(10);
        panel.add(field);

        JLabel dosage = new JLabel("Enter dosage:");
        panel.add(dosage);
        JTextField field1 = new JTextField(10);
        panel.add(field1);

        JLabel duration = new JLabel("Enter duration:");
        panel.add(duration);
        JTextField field3 = new JTextField(10);
        panel.add(field3);

        JLabel instructions = new JLabel("Enter instructions:");
        panel.add(instructions);
        JTextField field4 = new JTextField(10);
        panel.add(field4);

        JButton submitButton = new JButton("Submit");
        panel.add(new JLabel());
        panel.add(submitButton);
    }


}

class EnterVisitDetails extends JFrame {

    public JTextField dataField;
    public EnterVisitDetails() {
        setTitle("Enter Visit Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {


    }
}



