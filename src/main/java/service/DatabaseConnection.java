package service;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {
	
	private static HikariConfig config = new HikariConfig("/datasource.properties");
	private static HikariDataSource dataSource = new HikariDataSource(config);
	
	private static Connection conn;
	
	/**
	 * Method that returns the instance of the database connection
	 * (that should in theory be unique) and creates it if it doesn't exist.
	 * @return
	 */
	public static Connection getInstance() {
		if(conn == null) {
			try {
 				conn = dataSource.getConnection();
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return conn;
	}

}
