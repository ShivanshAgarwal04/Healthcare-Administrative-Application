import javax.swing.*;
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

    }

}

class EnterVisitDetails extends JFrame {
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



