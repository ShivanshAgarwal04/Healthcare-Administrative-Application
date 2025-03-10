import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class DoctorDashboardTest {

    @Test
    void testViewBookingsButton_Success() {
        DoctorDashboard dashboard = new DoctorDashboard();

        setTextFieldText(dashboard, "dateField", "2025-03-10");

        clickViewBookingsButton(dashboard);

        // Assert: Verify that the bookings area is updated with the correct data
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertEquals(
                "Booking Found:\n" +
                        "Date: 2025-03-10\n" +
                        "Time: 15:00:00\n",
                bookingsArea.getText(),
                "Bookings area should display the correct bookings."
        );
    }

    @Test
    void testViewBookingsButton_InvalidInput() {
        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering an invalid booking date
        setTextFieldText(dashboard, "dateField", "invalid-date");

        clickViewBookingsButton(dashboard);

        // Verify that the bookings area is not updated (or shows an error message)
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertTrue(bookingsArea.getText().isEmpty(), "Bookings area should be empty for invalid input.");
    }

    // Helper method
    private void setTextFieldText(DoctorDashboard dashboard, String fieldName, String text) {
        JTextField textField = (JTextField) findComponent(dashboard.getContentPane(), fieldName);
        if (textField != null) {
            textField.setText(text);
        } else {
            fail("JTextField with name " + fieldName + " not found.");
        }
    }

    // Helper method
    private void clickViewBookingsButton(DoctorDashboard dashboard) {
        JButton button = (JButton) findComponent(dashboard.getContentPane(), "View Bookings");
        if (button != null) {
            button.doClick();
        } else {
            fail("View Bookings button not found.");
        }
    }

    // Helper method
    private JTextArea findTextArea(DoctorDashboard dashboard) {
        return (JTextArea) findComponent(dashboard.getContentPane(), "bookingsArea");
    }

    private Component findComponent(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField && name.equals(component.getName())) {
                return component;
            }
            if (component instanceof JButton && name.equals(((JButton) component).getText())) {
                return component;
            }
            if (component instanceof JTextArea && name.equals(component.getName())) {
                return component;
            }
            if (component instanceof Container) {
                Component found = findComponent((Container) component, name);
                if (found != null) return found;
            }
        }
        return null;
    }
}
