package layout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DatabaseService {
	
	private Connection conn;
	private RequestData rd;
	private Statement stmt;
	private ResultSet results;
	
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
			int computer_id = rd.getComputerId();
			// execute the sql query
			results = stmt.executeQuery("SELECT * FROM computer WHERE id = " + computer_id);
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
			String name = "'" + rd.getComputer_name() + "'";
			String introduced = rd.getIntroducedDate() != null ? "'" + rd.getIntroducedDate() + "'": "" + rd.getIntroducedDate();
			String discontinued = rd.getDiscontinuedDate() != null ? "'" + rd.getDiscontinuedDate() + "'": "" + rd.getDiscontinuedDate();
			int company_id = rd.getCompany_id();
			String query = "INSERT INTO computer (name, introduced, discontinued, company_id) "
					+ "VALUES (" + name + ", " + introduced + ", " + discontinued + ", " + company_id + ")";
			// execute the sql update
			stmt.executeUpdate(query, Statement.NO_GENERATED_KEYS);
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
	/**
	 * Method that deletes a computer from the database
	 */
	public void deleteComputer() {
		try {
			// execute the sql update
			stmt.executeUpdate("DELETE FROM computer WHERE id = " + rd.getComputerId(), Statement.NO_GENERATED_KEYS);
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
			int computer_id = rd.getComputerId();
			String originalName = "";
			Timestamp originalIntroduced = null;
			Timestamp originalDiscontinued = null;
			int originalCompanyId = 0;
			
			// first retreive original data to update
			String query1 = "SELECT * FROM computer WHERE id = " + computer_id;
			results = stmt.executeQuery(query1);
			while(results.next()) {
				originalName = results.getString("name");
				originalIntroduced = results.getTimestamp("introduced");
				originalDiscontinued = results.getTimestamp("discontinued");
				originalCompanyId = results.getInt("company_id");
			}
			
			// tests which columns the user wants to update
			String name = rd.getComputer_name().isEmpty() ? "'" + originalName + "'" : "'" + rd.getComputer_name() + "'";
			String introduced = rd.getIntroducedDate() != null ? "'" + rd.getIntroducedDate() + "'": "" + originalIntroduced;
			String discontinued = rd.getDiscontinuedDate() != null ? "'" + rd.getDiscontinuedDate() + "'": "" + originalDiscontinued;
			int company_id = rd.getCompany_id() == 0 ? originalCompanyId : rd.getCompany_id();
			
			String query2 = "UPDATE computer "
					+ "SET name = " + name + ", introduced = " + introduced + ", discontinued = " + discontinued + ", company_id = " + company_id
					+ " WHERE id = " + computer_id;
			// execute the sql update
			stmt.executeUpdate(query2, Statement.NO_GENERATED_KEYS);
		}
		catch(SQLException e) {
			System.out.println("REQUEST FAILED");
		}
	}
	
}
