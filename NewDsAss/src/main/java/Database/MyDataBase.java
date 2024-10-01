package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
	
    static final String URL = "jdbc:mysql://localhost:3306/dsass";
    static final String USERNAME = "root";
    static final String PASSWORD = "host@123";
	
	public static Connection establishConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to MySQL database!");
        } catch (SQLException e) {
            System.err.println("Error on establishing connection: " + e.getMessage());
        }
        return connection;
    }
    
    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from MySQL database!");
            } catch (SQLException e) {
                System.err.println("Error disconnecting from MySQL database: " + e.getMessage());
            }
        }
    }
}