package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db";
	private static final String USER = "admincdb";
	private static final String PWD = "qwerty123";
	
	private static Connection conn;
	
	/**
	 * Method that returns the instance of the database connection
	 * (that should in theory be unique) and creates it if it doesn't exist.
	 * @return
	 */
	public static Connection getInstance() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection(URL, USER, PWD);
			}
			catch(SQLException e) {
				System.out.println("Database connection failed");
			}
		}
		return conn;
	}

}
