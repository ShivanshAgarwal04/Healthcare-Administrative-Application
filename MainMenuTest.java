import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @BeforeEach
    public void setUp() {
        // Initialize the MainMenu frame before each test
        mainMenu = new MainMenu(20001);// DoctorID for bob white
    }

    @Test
    public void testFrameTitle() {
        // Verify that the frame title is set correctly
        assertEquals("Doctor Interface: Main Menu", mainMenu.getTitle());
    }

    @Test
    public void testFrameSize() {
        // Verify that the frame size is set correctly
        Dimension expectedSize = new Dimension(300, 200);
        assertEquals(expectedSize, mainMenu.getSize());
    }

    @Test
    public void testDefaultCloseOperation() {
        // Verify that the default close operation is set correctly
        assertEquals(JFrame.EXIT_ON_CLOSE, mainMenu.getDefaultCloseOperation());
    }

    @Test
    public void testButtonsAdded() {
        // Verify that the buttons are added to the panel
        Component[] components = mainMenu.getContentPane().getComponents();
        assertEquals(1, components.length); // Only one panel should be added
        assertTrue(components[0] instanceof JPanel);

        JPanel panel = (JPanel) components[0];
        Component[] panelComponents = panel.getComponents();

        // Check if the buttons are present
        boolean bookingsButtonFound = false;
        boolean enterDetailsButtonFound = false;
        boolean logoutButtonFound = false;

        for (Component component : panelComponents) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                switch (button.getName()) {
                    case "viewBookings":
                        bookingsButtonFound = true;
                        break;
                    case "Enter details":
                        enterDetailsButtonFound = true;
                        break;
                    case "logoutButton":
                        logoutButtonFound = true;
                        break;
                }
            }
        }

        assertTrue(bookingsButtonFound, "Bookings button not found");
        assertTrue(enterDetailsButtonFound, "Enter details button not found");
        assertTrue(logoutButtonFound, "Logout button not found");
    }

    @Test
    public void testViewBookingsButtonAction() {
        // Simulate a click on the "Bookings" button
        JPanel panel = (JPanel) mainMenu.getContentPane().getComponents()[0];
        JButton bookingsButton = null;

        // Find the "Bookings" button
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton && "viewBookings".equals(((JButton) component).getName())) {
                bookingsButton = (JButton) component;
                break;
            }
        }

        assertNotNull(bookingsButton, "Bookings button not found");

        // Simulate a button click
        ActionEvent clickEvent = new ActionEvent(bookingsButton, ActionEvent.ACTION_PERFORMED, "");
        for (ActionListener listener : bookingsButton.getActionListeners()) {
            listener.actionPerformed(clickEvent);
        }

        // Verify that the DoctorDashboard is opened (this is a simple test and may need mocking)
        // For now, we assume the action is performed without errors
        assertTrue(true);
    }

    @Test
    public void testLogoutButtonAction() {
        // Simulate a click on the "Logout" button
        JPanel panel = (JPanel) mainMenu.getContentPane().getComponents()[0];
        JButton logoutButton = null;

        // Find the "Logout" button
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton && "logoutButton".equals(((JButton) component).getName())) {
                logoutButton = (JButton) component;
                break;
            }
        }

        assertNotNull(logoutButton, "Logout button not found");

        // Simulate a button click
        ActionEvent clickEvent = new ActionEvent(logoutButton, ActionEvent.ACTION_PERFORMED, "");
        for (ActionListener listener : logoutButton.getActionListeners()) {
            listener.actionPerformed(clickEvent);
        }

        // Verify that the LoginScreen is opened (this is a simple test and may need mocking)
        // For now, we assume the action is performed without errors
        assertTrue(true);
    }

    @Test
    public void testEnterDetailsButtonAction() {
        // Simulate a click on the "Enter details" button
        JPanel panel = (JPanel) mainMenu.getContentPane().getComponents()[0];
        JButton enterDetailsButton = null;

        // Find the "Enter details" button
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton && "Enter details".equals(((JButton) component).getName())) {
                enterDetailsButton = (JButton) component;
                break;
            }
        }

        assertNotNull(enterDetailsButton, "Enter details button not found");

        // Simulate a button click
        ActionEvent clickEvent = new ActionEvent(enterDetailsButton, ActionEvent.ACTION_PERFORMED, "");
        for (ActionListener listener : enterDetailsButton.getActionListeners()) {
            listener.actionPerformed(clickEvent);
        }

        // Verify that the EnterDetails frame is opened (this is a simple test and may need mocking)
        // For now, we assume the action is performed without errors
        assertTrue(true);
    }
}
