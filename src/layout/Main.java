package layout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection conn = null;
		final String URL = "jdbc:mysql://localhost:3306/computer-database-db";
		final String USER = "admincdb";
		final String PWD = "qwerty123";
		Scanner sc = new Scanner(System.in);
		DatabaseService ds = null;
		String s = "";
		RequestType rt = null;
		String[] data = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PWD);
			
			System.out.print("1 - list computers\n" +
								"2 - list companies\n" +
								"3 - show computer details\n" +
								"4 - create a computer\n" +
								"5 - delete a computer\n" +
								"6 - update a computer\n: ");
			
			s = sc.nextLine();
			switch(s.charAt(0)) {
			case '1':
				rt = RequestType.LIST_COMPUTERS;
				break;
			case '2':
				rt = RequestType.LIST_COMPANIES;
				break;
			case '3':
				rt = RequestType.SHOW_COMPUTER;
				data = new String[1];
				System.out.print("computer id : ");
				s = sc.nextLine();
				data[0] = s;
				break;
			case '4':
				rt = RequestType.CREATE;
				data = new String[4];
				System.out.print("computer name : ");
				s = sc.nextLine();
				data[0] = s;
				System.out.print("introduced date in format dd/mm/yyyy (n for null) : ");
				s = sc.nextLine();
				data[1] = s;
				System.out.print("discontinued date in format dd/mm/yyyy (n for null) : ");
				s = sc.nextLine();
				data[2] = s;
				System.out.print("company id : ");
				s = sc.nextLine();
				data[3] = s;
				break;
			case '5':
				rt = RequestType.DELETE;
				data = new String[1];
				System.out.print("computer id : ");
				s = sc.nextLine();
				data[0] = s;
				break;
			case '6':
				rt = RequestType.UPDATE;
				data = new String[4];
				System.out.print("computer name : ");
				s = sc.nextLine();
				data[0] = s;
				System.out.print("introduced date in format dd/mm/yyyy (n for null) : ");
				s = sc.nextLine();
				data[1] = s;
				System.out.print("discontinued date in format dd/mm/yyyy (n for null) : ");
				s = sc.nextLine();
				data[2] = s;
				System.out.print("company id : ");
				s = sc.nextLine();
				data[3] = s;
				break;
			default:
				throw new IllegalArgumentException();
			}
			ds = new DatabaseService(conn, new RequestData(rt, data));
			ds.executeRequest();
		}
		finally {
			sc.close();
			conn.close();
		}

	}

}
