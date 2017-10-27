package mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import model.Computer;

public class ComputerMapper {
	
	private static ComputerMapper instance = null;
	
	private ComputerMapper() {}
	
	public static ComputerMapper getInstance() {
		if(instance == null) {
			instance = new ComputerMapper();
		}
		return instance;
	}
	
	public Computer toEntity(ResultSet rs) {
		Computer computer = new Computer();
		try {
			computer.setName(rs.getString("name"));
			computer.setIntroduced(rs.getTimestamp("introduced"));
			computer.setDiscontinued(rs.getTimestamp("discontinued"));
			computer.setCompanyId(rs.getInt("company_id"));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
}
