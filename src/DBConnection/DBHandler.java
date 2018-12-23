package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler extends Configurations{

	Connection dbConnection;

	public Connection getConnection(){
		String connectionString = "jdbc:mysql://" + Configurations.dbhost + ":" + Configurations.dbport + "/" + Configurations.dbname +
				"?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbConnection = DriverManager.getConnection(connectionString, Configurations.dbuser, Configurations.dbpass);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dbConnection;
	}
}
