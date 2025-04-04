import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class AssignNewDoctorTest {
    private AssignNewDoctor assignNewDoctor;
    private TestDatabase testDB;
    private String lastDialogMessage;


    static class TestDatabase {
        private Connection connection;

        public TestDatabase() throws SQLException {
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            initializeDatabase();
        }

        private void initializeDatabase() throws SQLException {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE Doctors (doctorID INT PRIMARY KEY, doctorName VARCHAR(100))");
                stmt.execute("CREATE TABLE Bookings (bookingNo INT PRIMARY KEY, doctorID INT)");
                stmt.execute("INSERT INTO Doctors VALUES (101, 'Dr. Smith'), (102, 'Dr. Johnson')");
                stmt.execute("INSERT INTO Bookings VALUES (123, NULL)");
            }
        }

        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        testDB = new TestDatabase();

        DBConnection.setTestConnection(testDB.getConnection());


        lastDialogMessage = null;
        assignNewDoctor = new AssignNewDoctor(123) {
            @Override
            protected void showMessageDialog(String message) {
                lastDialogMessage = message;
            }
        };
    }

    @AfterEach
    void tearDown() throws SQLException {
        assignNewDoctor.dispose();
        try (Statement stmt = testDB.getConnection().createStatement()) {
            stmt.execute("DROP ALL OBJECTS");
        }
        testDB.getConnection().close();
    }

    @Test
    void constructor_InitializesUIComponents() {
        assertNotNull(assignNewDoctor.doctorList);
        assertNotNull(assignNewDoctor.doctorIDs);
        assertEquals("Assign New Doctor", assignNewDoctor.getTitle());
    }

    @Test
    void loadDoctors_PopulatesComboBoxFromDatabase() {
        assertEquals(2, assignNewDoctor.doctorList.getItemCount());
        assertEquals("Dr. Smith", assignNewDoctor.doctorList.getItemAt(0));
        assertEquals("Dr. Johnson", assignNewDoctor.doctorList.getItemAt(1));
        assertEquals(2, assignNewDoctor.doctorIDs.size());
    }

    @Test
    void assignDoctor_WithValidSelection_UpdatesBookingRecord() throws SQLException {
        assignNewDoctor.doctorList.setSelectedIndex(0); // Select Dr. Smith

        assignNewDoctor.assignDoctor();


        try (Statement stmt = testDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT doctorID FROM Bookings WHERE bookingNo = 123")) {
            assertTrue(rs.next());
            assertEquals(101, rs.getInt("doctorID"));
        }


        assertEquals("Doctor successfully assigned.", lastDialogMessage);
    }

    @Test
    void assignDoctor_WithNoSelection_ShowsErrorDialog() {
        assignNewDoctor.doctorList.setSelectedIndex(-1);
        assignNewDoctor.assignDoctor();
        assertEquals("Please select a doctor.", lastDialogMessage);
    }

    @Test
    void windowCloses_AfterSuccessfulAssignment() {
        assignNewDoctor.doctorList.setSelectedIndex(0);
        assignNewDoctor.assignDoctor();
        assertFalse(assignNewDoctor.isVisible());
    }
}