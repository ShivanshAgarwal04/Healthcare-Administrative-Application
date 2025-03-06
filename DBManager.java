import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public static void main(String[] args) {
		DBManager db = new DBManager();		
		db.testConnection();
	}
	
	private void testConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/doctorinterface?user=sagarwal&password=softwaredev");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from assessments");
			while (resultSet.next())
				System.out.println(resultSet.getString("ModuleID") + " - " + resultSet.getString("AssessmentID") + " - " + resultSet.getString("AssessmentDescription"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}