import java.sql.*;

// Database Connection
class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/testdatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "Password.123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static boolean confirmBookingID(Integer bookingNo){
        boolean isValid = false;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT bookingNo FROM Bookings WHERE bookingNo = ?")) {
            statement.setInt(1, bookingNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    isValid = (bookingNo.equals(resultSet.getInt("bookingNo")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static void setTestConnection(Connection connection) {
    }
    public static int getDoctorID(String username, String password) {
        int doctorID = -1; // Default to -1
        String query = "SELECT doctorID FROM DoctorCredentials WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Hash password if needed

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                doctorID = rs.getInt("doctorID"); // Get doctorID from DB
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return doctorID;
    }

    public static String getDoctorName(int doctorID) {
        String doctorName = null;
        String query = "SELECT doctorName FROM Doctors WHERE doctorID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, doctorID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                doctorName = rs.getString("doctorName");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return doctorName;
    }


}