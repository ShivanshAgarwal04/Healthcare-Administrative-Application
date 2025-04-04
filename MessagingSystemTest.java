import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagingSystemTest {

    private TestMainMenu testMainMenu;
    private MessagingSystem messagingSystem;

    // Manual mock implementation of MainMenu
    static class TestMainMenu extends MainMenu {
        private static final Integer doctorID = 20001; // doctorID for
        public String lastReceivedMessage;
        public boolean wasMessageUpdated = false;

        public TestMainMenu() {
            super(doctorID);
        }

        @Override
        public void updateMessage(String message) {
            this.lastReceivedMessage = message;
            this.wasMessageUpdated = true;
        }
    }

    @BeforeEach
    void setUp() {
        testMainMenu = new TestMainMenu();
        messagingSystem = new MessagingSystem(testMainMenu);
    }

    // --- TESTS FOR sendMessage() ---
    @Test
    void sendMessage_WithAllFields_UpdatesMainMenuWithFormattedMessage() {
        messagingSystem.recipientField.setText("doctor@hospital.com");
        messagingSystem.patientField.setText("John Smith");
        messagingSystem.messageBody.setText("Please review test results.");

        messagingSystem.sendMessage();

        assertEquals(
                "To: doctor@hospital.com | Please review test results. (Regarding Patient: John Smith)",
                testMainMenu.lastReceivedMessage
        );
        assertTrue(testMainMenu.wasMessageUpdated);
    }

    @Test
    void sendMessage_WithoutPatientField_UpdatesMainMenuWithoutPatientInfo() {
        messagingSystem.recipientField.setText("nurse@clinic.com");
        messagingSystem.messageBody.setText("Medication refill needed.");

        messagingSystem.sendMessage();

        assertEquals(
                "To: nurse@clinic.com | Medication refill needed.",
                testMainMenu.lastReceivedMessage
        );
    }

    @Test
    void sendMessage_WithEmptyRecipient_ShowsErrorDialog() {
        messagingSystem.recipientField.setText("");
        messagingSystem.messageBody.setText("This should fail.");

        // Simulate button click
        for (ActionListener listener : messagingSystem.sendButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertFalse(testMainMenu.wasMessageUpdated, "MainMenu should not be updated");
        assertTrue(messagingSystem.isVisible(), "Window should stay open on error");
    }

    @Test
    void sendMessage_WithEmptyMessageBody_ShowsErrorDialog() {
        messagingSystem.recipientField.setText("admin@hospital.com");
        messagingSystem.messageBody.setText("");

        messagingSystem.sendMessage();

        assertFalse(testMainMenu.wasMessageUpdated);
        assertTrue(messagingSystem.isVisible());
    }

    // --- TESTS FOR UI COMPONENTS ---
    @Test
    void uiComponents_AreCorrectlyInitialized() {
        assertNotNull(messagingSystem.recipientField, "Recipient field missing");
        assertNotNull(messagingSystem.patientField, "Patient field missing");
        assertNotNull(messagingSystem.messageBody, "Message body missing");
        assertNotNull(messagingSystem.sendButton, "Send button missing");
    }

    @Test
    void windowCloses_AfterSuccessfulMessageSend() {
        messagingSystem.recipientField.setText("test@example.com");
        messagingSystem.messageBody.setText("Test message");

        messagingSystem.sendMessage();

        assertFalse(messagingSystem.isVisible(), "Window should close after send");
    }
}