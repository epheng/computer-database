package mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.CompanyDAO;
import dto.ComputerDTO;
import model.Computer;

@Component
public class ComputerMapper {
	
	@Autowired
	CompanyDAO companyDao;
	
	public Computer toEntity(ResultSet rs) {
		Computer computer = new Computer();
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
		String company = computer.getCompanyId() == 0 ? "" : companyDao.getCompanyById(computer.getCompanyId()).getName();
		return new ComputerDTO(id, name, introduced, discontinued, company);
	}
	
	public Timestamp parseTimestamp(String s) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date parsedDate = dateFormat.parse(s);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			return timestamp;
		}
		catch(ParseException e) {
			return null;
		}
	}
	
	public Computer toComputer(ComputerDTO computerDto) {
		String name = computerDto.getName();
		Timestamp introduced = parseTimestamp(computerDto.getIntroduced());
		Timestamp discontinued = parseTimestamp(computerDto.getDiscontinued());
		int companyId = companyDao.getCompanyIdByName(computerDto.getCompany());
		String computerId = computerDto.getId();
		if(computerId == null) {
			return new Computer(name, introduced, discontinued, companyId);
		} else {
			return new Computer(Integer.parseInt(computerId), name, introduced, discontinued, companyId);
		}
	}
	
}
