package layout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
		
		try {
			Connection c = DriverManager.getConnection(
					"jdbc:mysql://localhost/computer-database-db?"
							+ "user=admincdb&password=qwerty123");
			
			System.out.println("test");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
