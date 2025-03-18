import javax.swing.*;

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
    private void placeComponents(JPanel panel){

    }
}
