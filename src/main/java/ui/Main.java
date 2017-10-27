package ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import service.DatabaseConnection;
import service.DatabaseService;
import service.RequestData;
import service.RequestType;

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
		
		DatabaseService ds = null;
		String s = "";
		RequestType rt = null;
		String[] data = null;
		
		try {
			conn = DatabaseConnection.getInstance();
			
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
					while(s.isEmpty()) {
						System.out.println("computer id can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					break;
				case '4':
					rt = RequestType.CREATE;
					data = new String[4];
					System.out.print("computer name : ");
					s = sc.nextLine();
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
					rt = RequestType.DELETE;
					data = new String[1];
					System.out.print("computer id : ");
					s = sc.nextLine();
					while(s.isEmpty()) {
						System.out.println("computer id can't be empty : ");
						s = sc.nextLine();
					}
					data[0] = s;
					break;
				case '6':
					rt = RequestType.UPDATE;
					data = new String[5];
					System.out.print("computer id : ");
					s = sc.nextLine();
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
				ds = new DatabaseService(new RequestData(rt, data));
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
			sc.close();
			conn.close();
		}

	}

}
