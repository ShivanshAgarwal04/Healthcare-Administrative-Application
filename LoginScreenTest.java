import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginScreenTest {

    @Test
    void testLoginButton_Success() {

        LoginScreen loginScreen = new LoginScreen();

        // Simulate entering a username and password
        setTextFieldText(loginScreen, "usernameField", "validUser");
        setPasswordFieldText(loginScreen, "passwordField", "validPassword");


        clickLoginButton(loginScreen);


        // Verify that the login screen closes after successful login
        assertFalse(loginScreen.isVisible(), "Login screen should close after successful login.");
    }

    @Test
    void testLoginButton_Failure_WrongPassword() {

        LoginScreen loginScreen = new LoginScreen();

        // Simulate entering a username and incorrect password
        setTextFieldText(loginScreen, "usernameField", "validUser");
        setPasswordFieldText(loginScreen, "passwordField", "wrongPassword");


        clickLoginButton(loginScreen);


        // Verify that an error dialog is displayed
        JOptionPane errorDialog = getErrorDialog(loginScreen);
        assertNotNull(errorDialog, "An error dialog should be displayed for incorrect password.");
        assertEquals("Invalid username or password.", errorDialog.getMessage(), "Error message should match.");
    }

    @Test
    void testLoginButton_Failure_UserNotFound() {

        LoginScreen loginScreen = new LoginScreen();

        // Simulate entering a non-existent username and password
        setTextFieldText(loginScreen, "usernameField", "nonExistentUser");
        setPasswordFieldText(loginScreen, "passwordField", "testPassword");


        clickLoginButton(loginScreen);


        // Verify that an error dialog is displayed
        JOptionPane errorDialog = getErrorDialog(loginScreen);
        assertNotNull(errorDialog, "An error dialog should be displayed for non-existent user.");
        assertEquals("Invalid username or password.", errorDialog.getMessage(), "Error message should match.");
    }

    // Helper method to set text in a JTextField
    private void setTextFieldText(LoginScreen loginScreen, String fieldName, String text) {
        Component component = findComponentByName(loginScreen, fieldName);
        if (component instanceof JTextField) {
            ((JTextField) component).setText(text);
        } else {
            fail("JTextField with name " + fieldName + " not found.");
        }
    }

    // Helper method to set text in a JPasswordField
    private void setPasswordFieldText(LoginScreen loginScreen, String fieldName, String text) {
        Component component = findComponentByName(loginScreen, fieldName);
        if (component instanceof JPasswordField) {
            ((JPasswordField) component).setText(text);
        } else {
            fail("JPasswordField with name " + fieldName + " not found.");
        }
    }

    // Helper method to click the login button
    private void clickLoginButton(LoginScreen loginScreen) {
        Component component = findComponentByName(loginScreen, "loginButton");
        if (component instanceof JButton) {
            ((JButton) component).doClick();
        } else {
            fail("Login button not found.");
        }
    }

    // Helper method to find a component by name
    private Component findComponentByName(LoginScreen loginScreen, String name) {
        for (Component component : loginScreen.getContentPane().getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }

    // Helper method to get the error dialog
    private JOptionPane getErrorDialog(LoginScreen loginScreen) {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponent(0) instanceof JOptionPane) {
                    return (JOptionPane) dialog.getContentPane().getComponent(0);
                }
            }
        }
        return null;
    }
}