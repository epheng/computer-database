package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import model.RequestData;
import ui.Main;
import ui.Page;

public class DatabaseService {
	
	private Connection conn;
	private RequestData rd;
	private Statement stmt;
	private ResultSet results;
	private PreparedStatement prepStmt;
	
	public DatabaseService(Connection conn, RequestData rd) {
		this.conn = conn;
		this.rd = rd;
	}
	
	/**
	 * Method that creates a statement and
	 * calls the appropriate execution method depending on the request type
	 * @throws SQLException
	 */
	public void executeRequest() throws SQLException {
		try {
			// create a statement
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
	
	/**
	 * Method that retreives the list of computers from the database
	 * and display it on pages of 10 elements at a time.
	 */
	public void listComputers() {
		try {
			// execute the sql query
			results = stmt.executeQuery("SELECT * FROM computer");
			
			Page page = new Page(results);
			System.out.print("press n to display the next 10 computers : ");
			String s = Main.sc.nextLine();
			while(!s.isEmpty() && s.charAt(0) == 'n') {
				// displays the next 10 elements
				page.afficheNext();
				System.out.print("press n to display the next 10 computers : ");
				s = Main.sc.nextLine();
			}
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
	/**
	 * Method that retreives the list of companies from the database
	 * and display it.
	 */
	public void listCompanies() {
		try {
			// execute the sql query
			results = stmt.executeQuery("SELECT * FROM company");
			while(results.next()) {
				System.out.println(results.getString("name"));
			}
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
	/**
	 * Method that displays the details of a computer
	 */
	public void showComputer() {
		try {
			int computerId = rd.getComputerId();
			// execute the sql query
			String query = "SELECT * FROM computer WHERE id = ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, computerId);
			results = prepStmt.executeQuery();
			while(results.next()) {
				System.out.println("Name : " + results.getString("name") +
									"\nIntroduced date : " + results.getString("introduced") +
									"\nDiscontinued date : " + results.getString("discontinued") +
									"\nCompany id : " + results.getString("company_id"));
			}
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
	/**
	 * Method thats insert a new computer in the database
	 */
	public void createComputer() {
		try {
			// execute the sql update
			String query = "INSERT INTO computer (name, introduced, discontinued, company_id)"
					+ " VALUES (?, ?, ?, ?)";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString(1, rd.getComputer_name());
			prepStmt.setTimestamp(2, rd.getIntroducedDate());
			prepStmt.setTimestamp(3, rd.getDiscontinuedDate());
			prepStmt.setInt(4, rd.getCompany_id());
			int state = prepStmt.executeUpdate();
			if(state != 1)
				System.out.println("INSERT FAILED");
			else
				System.out.println("INSERT SUCCESS");
		}
		catch(SQLException e) {
			System.out.println(e.getMessage() + " REQUEST FAILED");
		}
	}
	
	/**
	 * Method that deletes a computer from the database
	 */
	public void deleteComputer() {
		try {
			// execute the sql update
			String query = "DELETE FROM computer WHERE id = ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, rd.getComputerId());
			int state = prepStmt.executeUpdate();
			if(state != 1)
				System.out.println("DELETION FAILED");
			else
				System.out.println("DELETION SUCCESS");
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
	/**
	 * Method that updates a computer in the database
	 */
	public void updateComputer() {
		try {
			int computerId = rd.getComputerId();
			String originalName = "";
			Timestamp originalIntroduced = null;
			Timestamp originalDiscontinued = null;
			int originalCompanyId = 0;
			
			// first retreive original data to update
			String query1 = "SELECT * FROM computer WHERE id = ?";
			prepStmt = conn.prepareStatement(query1);
			prepStmt.setInt(1, computerId);
			results = prepStmt.executeQuery();
			while(results.next()) {
				originalName = results.getString("name");
				originalIntroduced = results.getTimestamp("introduced");
				originalDiscontinued = results.getTimestamp("discontinued");
				originalCompanyId = results.getInt("company_id");
			}
			
			// tests which columns the user wants to update
			String name = rd.getComputer_name().isEmpty() ? originalName : rd.getComputer_name();
			Timestamp introduced = rd.getIntroducedDate() != null ? rd.getIntroducedDate() : originalIntroduced;
			Timestamp discontinued = rd.getDiscontinuedDate() != null ? rd.getDiscontinuedDate() : originalDiscontinued;
			int companyId = rd.getCompany_id() == 0 ? originalCompanyId : rd.getCompany_id();
			
			String query2 = "UPDATE computer SET name =?, introduced =?, discontinued =?, company_id =? WHERE id =?";
			prepStmt = conn.prepareStatement(query2);
			prepStmt.setString(1, name);
			prepStmt.setTimestamp(2, introduced);
			prepStmt.setTimestamp(3, discontinued);
			prepStmt.setInt(4, companyId);
			prepStmt.setInt(5, computerId);
			// execute the sql update
			System.out.println(prepStmt.toString());
			int state = prepStmt.executeUpdate();
			if(state != 1)
				System.out.println("UPDATE FAILED");
			else
				System.out.println("UPDATE SUCCESS");
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
			e.printStackTrace();
		}
	}
	
}
