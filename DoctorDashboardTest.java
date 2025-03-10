import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class DoctorDashboardTest {

    @Test
    void testViewBookingsButton_Success() {

        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering a month and year
        setTextFieldText(dashboard, "monthField", "10");
        setTextFieldText(dashboard, "yearField", "2023");


        clickViewBookingsButton(dashboard);

        // Assert
        // Verify that the bookings area is updated with the correct data
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertEquals("Booking 1: 2023-10-15 10:00 AM\nBooking 2: 2023-10-20 02:00 PM", bookingsArea.getText(), "Bookings area should display the correct bookings.");
    }

    @Test
    void testViewBookingsButton_InvalidInput() {

        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering invalid month and year
        setTextFieldText(dashboard, "monthField", "invalid");
        setTextFieldText(dashboard, "yearField", "invalid");


        clickViewBookingsButton(dashboard);


        // Verify that the bookings area is not updated (or shows an error message)
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertTrue(bookingsArea.getText().isEmpty(), "Bookings area should be empty for invalid input.");
    }

    // Helper method to set text in a JTextField
    private void setTextFieldText(DoctorDashboard dashboard, String fieldName, String text) {
        for (Component component : dashboard.getContentPane().getComponents()) {
            if (component instanceof JTextField && fieldName.equals(component.getName())) {
                ((JTextField) component).setText(text);
                return;
            }
        }
        fail("JTextField with name " + fieldName + " not found.");
    }

    // Helper method to click the "View Bookings" button
    private void clickViewBookingsButton(DoctorDashboard dashboard) {
        for (Component component : dashboard.getContentPane().getComponents()) {
            if (component instanceof JButton && "View Bookings".equals(((JButton) component).getText())) {
                ((JButton) component).doClick();
                return;
            }
        }
        fail("View Bookings button not found.");
    }

    // Helper method to find the JTextArea
    private JTextArea findTextArea(DoctorDashboard dashboard) {
        for (Component component : dashboard.getContentPane().getComponents()) {
            if (component instanceof JTextArea) {
                return (JTextArea) component;
            }
        }
        return null;
    }
}
