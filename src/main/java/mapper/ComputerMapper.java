package mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.CompanyDAO;
import dto.ComputerDTO;
import model.Computer;

public class ComputerMapper {
	
	private static ComputerMapper instance = null;
	public static ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	
	private ComputerMapper() {}
	
	public static ComputerMapper getInstance() {
		if(instance == null) {
			instance = new ComputerMapper();
		}
		return instance;
	}
	
	public Computer toEntity(ResultSet rs) {
		Computer computer = (Computer) context.getBean("computer");
		try {
			computer.setId(rs.getInt("id"));
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
	
	public String removeDateTime(String date) {
		String[] parts = date.split(" ");
		return parts[0];
	}
	
	public ComputerDTO toComputerDTO(Computer computer) {
		String id = "" + computer.getId();
		String name = computer.getName();
		String introduced = computer.getIntroduced() == null ? "" : removeDateTime(computer.getIntroduced().toString());
		String discontinued = computer.getDiscontinued() == null ? "" : removeDateTime(computer.getDiscontinued().toString());
		String company = computer.getCompanyId() == 0 ? "" : CompanyDAO.getInstance().getCompanyById(computer.getCompanyId()).getName();
		return new ComputerDTO(id, name, introduced, discontinued, company);
	}
	
}
