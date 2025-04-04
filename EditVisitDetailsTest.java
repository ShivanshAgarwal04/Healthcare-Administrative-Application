import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class EditVisitDetailsTest {
    private EditVisitDetails editVisitDetails;
    private static Connection testConnection;
    private String lastDialogMessage;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        testConnection = DBConnection.getConnection();


        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("CREATE TABLE Visits (visitID INT PRIMARY KEY, bookingNo INT, notes VARCHAR(1000))");
            stmt.execute("CREATE TABLE Prescriptions (prescriptionID INT PRIMARY KEY, bookingNo INT, " +
                    "medicationName VARCHAR(100), dosage VARCHAR(50), duration VARCHAR(50), instructions VARCHAR(500))");


            stmt.execute("INSERT INTO Visits VALUES (1, 123, 'Initial checkup notes')");
            stmt.execute("INSERT INTO Prescriptions VALUES (101, 123, 'Ibuprofen', '200mg', '7 days', 'Take with food')");
        }


        DBConnection.setTestConnection(testConnection);
    }

    @BeforeEach
    void setUp() {
        // Initialize with dialog capture
        lastDialogMessage = null;
        editVisitDetails = new EditVisitDetails(123) {
            @Override
            protected void showMessageDialog(String message) {
                lastDialogMessage = message;
            }
        };
    }

    @AfterEach
    void tearDown() {
        editVisitDetails.dispose();
    }

    @AfterAll
    static void cleanUp() throws SQLException {
        if (testConnection != null) {
            testConnection.close();
        }
    }

    @Test
    void constructor_InitializesUIComponents() {
        assertNotNull(editVisitDetails.notesField);
        assertNotNull(editVisitDetails.medicationField);
        assertNotNull(editVisitDetails.dosageField);
        assertNotNull(editVisitDetails.durationField);
        assertNotNull(editVisitDetails.instructionsField);
        assertEquals("Edit Visit and Prescription Details", editVisitDetails.getTitle());
    }

    @Test
    void loadExistingDetails_LoadsDataCorrectly() {
        assertEquals("Initial checkup notes", editVisitDetails.notesField.getText());
        assertEquals("Ibuprofen", editVisitDetails.medicationField.getText());
        assertEquals("200mg", editVisitDetails.dosageField.getText());
        assertEquals("7 days", editVisitDetails.durationField.getText());
        assertEquals("Take with food", editVisitDetails.instructionsField.getText());
    }

    @Test
    void loadExistingDetails_HandlesMissingVisit() {
        EditVisitDetails invalid = new EditVisitDetails(999) {
            @Override
            protected void showMessageDialog(String message) {
                lastDialogMessage = message;
            }
        };
        assertEquals("No visit found for the given Booking ID.", lastDialogMessage);
        assertFalse(invalid.isVisible());
    }

    @Test
    void updateDetails_UpdatesDatabaseSuccessfully() throws SQLException {
        // Modify fields
        editVisitDetails.notesField.setText("Updated notes");
        editVisitDetails.medicationField.setText("Paracetamol");

        editVisitDetails.updateDetails();

        // Verify database updates
        try (Statement stmt = testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT notes FROM Visits WHERE visitID = 1")) {
            assertTrue(rs.next());
            assertEquals("Updated notes", rs.getString("notes"));
        }

        try (Statement stmt = testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT medicationName FROM Prescriptions WHERE prescriptionID = 101")) {
            assertTrue(rs.next());
            assertEquals("Paracetamol", rs.getString("medicationName"));
        }

        assertEquals("Visit and Prescription details updated successfully!", lastDialogMessage);
        assertFalse(editVisitDetails.isVisible());
    }

    @Test
    void updateDetails_ValidatesEmptyFields() {

        editVisitDetails.notesField.setText("");
        editVisitDetails.medicationField.setText("");

        editVisitDetails.updateDetails();

        assertEquals("All fields must be filled!", lastDialogMessage);
        assertTrue(editVisitDetails.isVisible());
    }

    @Test
    void sendConfirmationMessage_ShowsDialog() {
        editVisitDetails.sendConfirmationMessage();
        assertEquals("Confirmation sent to doctor and patient.", lastDialogMessage);
    }
}
