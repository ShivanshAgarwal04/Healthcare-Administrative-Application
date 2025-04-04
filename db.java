import java.sql.*;

// Database Connection
class DBConnection {
    private static final String URL = "jbdc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

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
}