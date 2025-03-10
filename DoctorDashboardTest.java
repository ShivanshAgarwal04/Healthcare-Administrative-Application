import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class DoctorDashboardTest {

    @Test
    void testViewBookingsButton_Success() {

        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering a valid date and time
        setTextFieldText(dashboard, "dateField", "2023-10-15");
        setTextFieldText(dashboard, "timeField", "10:00");


        clickViewBookingsButton(dashboard);


        // Verify that the bookings area is updated with the correct data
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertTrue(bookingsArea.getText().contains("Booking Found:"), "Bookings area should display booking details.");
    }

    @Test
    void testViewBookingsButton_NoBookingFound() {

        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering a date and time with no bookings
        setTextFieldText(dashboard, "dateField", "2023-10-16");
        setTextFieldText(dashboard, "timeField", "10:00");


        clickViewBookingsButton(dashboard);


        // Verify that the bookings area displays "No booking found"
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertTrue(bookingsArea.getText().contains("No booking found"), "Bookings area should display 'No booking found'.");
    }

    @Test
    void testViewBookingsButton_InvalidInput() {

        DoctorDashboard dashboard = new DoctorDashboard();

        // Simulate entering invalid date and time
        setTextFieldText(dashboard, "dateField", "invalid-date");
        setTextFieldText(dashboard, "timeField", "invalid-time");


        clickViewBookingsButton(dashboard);


        // Verify that the bookings area displays an error message
        JTextArea bookingsArea = findTextArea(dashboard);
        assertNotNull(bookingsArea, "Bookings area should not be null.");
        assertTrue(bookingsArea.getText().contains("Error retrieving data"), "Bookings area should display an error message.");
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

    // Helper method to click the "Find Booking" button
    private void clickViewBookingsButton(DoctorDashboard dashboard) {
        for (Component component : dashboard.getContentPane().getComponents()) {
            if (component instanceof JButton && "Find Booking".equals(((JButton) component).getText())) {
                ((JButton) component).doClick();
                return;
            }
        }
        fail("Find Booking button not found.");
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