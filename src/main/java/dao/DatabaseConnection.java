package dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class DatabaseConnection {
	
	@Autowired
	private HikariDataSource dataSource;
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}
