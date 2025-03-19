import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class EnterDetailsTest {

    private EnterDetails enterDetails;

    @BeforeEach
    public void setUp() {
        // Initialize the EnterDetails frame before each test
        enterDetails = new EnterDetails();
    }

    @Test
    public void testFrameTitle() {
        // Verify that the frame title is set correctly
        assertEquals("Enter visit details & prescriptions", enterDetails.getTitle());
    }

    @Test
    public void testFrameSize() {
        // Verify that the frame size is set correctly
        Dimension expectedSize = new Dimension(400, 300);
        assertEquals(expectedSize, enterDetails.getSize());
    }

    @Test
    public void testDefaultCloseOperation() {
        // Verify that the default close operation is set correctly
        assertEquals(JFrame.EXIT_ON_CLOSE, enterDetails.getDefaultCloseOperation());
    }

    @Test
    public void testPanelAdded() {
        // Verify that a JPanel is added to the frame
        Component[] components = enterDetails.getContentPane().getComponents();
        assertEquals(1, components.length);
        assertTrue(components[0] instanceof JPanel);
    }
}
