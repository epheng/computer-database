package layout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
	
	private Connection conn;
	private RequestData rd;
	private Statement stmt;
	private ResultSet results;
	
	public DatabaseService(Connection conn, RequestData rd) {
		this.conn = conn;
		this.rd = rd;
	}
	
	public void executeRequest() throws SQLException {
		try {
			stmt = conn.createStatement();
			switch(rd.getRequestType()) {
			case LIST_COMPUTERS:
				listComputers();
				break;
			case LIST_COMPANIES:
				listCompanies();
				break;
			case SHOW_COMPUTER:
				showComputer();
				break;
			case CREATE:
				createComputer();
				break;
			case DELETE:
				deleteComputer();
				break;
			case UPDATE:
				updateComputer();
				break;
			}
		}
		finally {
			stmt.close();
		}
	}
	
	public void listComputers() throws SQLException {
		results = stmt.executeQuery("SELECT * FROM computer");
		while(results.next()) {
			System.out.println(results.getString("name"));
		}
	}
	
	public void listCompanies() throws SQLException {
		results = stmt.executeQuery("SELECT * FROM company");
		while(results.next()) {
			System.out.println(results.getString("name"));
		}
	}
	
	public void showComputer() throws SQLException {
		int computer_id = rd.getComputerId();
		results = stmt.executeQuery("SELECT * FROM computer WHERE id = " + computer_id);
		while(results.next()) {
			System.out.println("Name : " + results.getString("name") +
								"\nIntroduced date : " + results.getString("introduced") +
								"\nDiscontinued date : " + results.getString("discontinued") +
								"\nCompany id : " + results.getString("company_id"));
		}
	}
	
	public void createComputer() throws SQLException {
		String name = "'" + rd.getComputer_name() + "'";
		String introduced = rd.getIntroducedDate() != null ? "'" + rd.getIntroducedDate() + "'": "" + rd.getIntroducedDate();
		String discontinued = rd.getDiscontinuedDate() != null ? "'" + rd.getDiscontinuedDate() + "'": "" + rd.getDiscontinuedDate();
		int company_id = rd.getCompany_id();
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (" + name + ", " + introduced + ", " + discontinued + ", " + company_id + ")";
		System.out.println(query);
		stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
	}
	
	public void deleteComputer() throws SQLException {
		stmt.executeUpdate("DELETE FROM computer WHERE id = " + rd.getComputerId(), Statement.RETURN_GENERATED_KEYS);		
	}
	
	public void updateComputer() throws SQLException {
		
		results = stmt.executeQuery("");
		while(results.next()) {
			
		}
	}
	
}
