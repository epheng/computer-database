package tmp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Page {
	
	private ResultSet rs;
	
	public Page(ResultSet rs) {
		this.rs = rs;
	}
	
	/**
	 * Displays the next 10 elements in the resultset
	 */
	public void afficheNext() {
		try {
			int i = 0;
			while(i < 10 && rs.next()) {
				System.out.println(rs.getString("name"));
				i++;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
