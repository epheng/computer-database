package layout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	public static Scanner sc = new Scanner(System.in);
	
	/**
	 * Establishes a database connection and
	 * displays a CLI on the console to allow the user to
	 * list, add, delete or update data in the database
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection conn = null;
		final String URL = "jdbc:mysql://localhost:3306/computer-database-db";
		final String USER = "admincdb";
		final String PWD = "qwerty123";
		
		DatabaseService ds = null;
		String s = "";
		RequestType rt = null;
		String[] data = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Connection to the database
			conn = DriverManager.getConnection(URL, USER, PWD);
			
			// List of options
			System.out.print("1 - list computers\n" +
					"2 - list companies\n" +
					"3 - show computer details\n" +
					"4 - create a computer\n" +
					"5 - delete a computer\n" +
					"6 - update a computer\n: ");
			while(sc.hasNext()) {
				s = sc.nextLine();
				switch(s.charAt(0)) {
				case '1':
					// case where the user lists the computers
					rt = RequestType.LIST_COMPUTERS;
					break;
				case '2':
					// case where the user lists the companies
					rt = RequestType.LIST_COMPANIES;
					break;
				case '3':
					// case where the user displays a computer details
					rt = RequestType.SHOW_COMPUTER;
					data = new String[1];
					System.out.print("computer id : ");
					s = sc.nextLine();
					// User input cannot be empty
					while(s.isEmpty()) {
						System.out.println("computer id can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					break;
				case '4':
					// case where the user inserts a new computer in the database
					rt = RequestType.CREATE;
					data = new String[4];
					System.out.print("computer name : ");
					s = sc.nextLine();
					// User input cannot be empty
					while(s.isEmpty()) {
						System.out.println("computer name can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					System.out.print("introduced date in format dd/mm/yyyy : ");
					s = sc.nextLine();
					data[1] = s;
					System.out.print("discontinued date in format dd/mm/yyyy : ");
					s = sc.nextLine();
					data[2] = s;
					System.out.print("company id : ");
					s = sc.nextLine();
					data[3] = s;
					break;
				case '5':
					// case where the user deletes a computer from the database
					rt = RequestType.DELETE;
					data = new String[1];
					System.out.print("computer id : ");
					s = sc.nextLine();
					// User input cannot be empty
					while(s.isEmpty()) {
						System.out.println("computer id can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					break;
				case '6':
					// case where the user updates the details of a computer
					rt = RequestType.UPDATE;
					data = new String[5];
					System.out.print("computer id : ");
					s = sc.nextLine();
					// User input cannot be empty
					while(s.isEmpty()) {
						System.out.println("computer id can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					System.out.println("(press enter if you don't change)");
					System.out.print("computer name : ");
					s = sc.nextLine();
					data[1] = s;
					System.out.print("introduced date in format dd/mm/yyyy : ");
					s = sc.nextLine();
					data[2] = s;
					System.out.print("discontinued date in format dd/mm/yyyy : ");
					s = sc.nextLine();
					data[3] = s;
					System.out.print("company id : ");
					s = sc.nextLine();
					data[4] = s;
					break;
				default:
					System.out.print("enter 1 to 6 : ");
					continue;
				}
				ds = new DatabaseService(conn, new RequestData(rt, data));
				ds.executeRequest();
				System.out.print("\n1 - list computers\n" +
						"2 - list companies\n" +
						"3 - show computer details\n" +
						"4 - create a computer\n" +
						"5 - delete a computer\n" +
						"6 - update a computer\n: ");
			}
		}
		finally {
			// closing stuff
			sc.close();
			conn.close();
		}

	}

}
