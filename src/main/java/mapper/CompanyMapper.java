package mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import dto.CompanyDTO;
import model.Company;

public class CompanyMapper {
	
	private static CompanyMapper instance = null;
	
	private CompanyMapper() {}
	
	public static CompanyMapper getInstance() {
		if(instance == null) {
			instance = new CompanyMapper();
		}
		return instance;
	}
	
	public Company toEntity(ResultSet rs) {
		Company company = new Company();
		try {
			company.setId(rs.getInt("id"));
			company.setName(rs.getString("name"));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return company;
	}
	
	public CompanyDTO toCompanyDTO(Company company) {
		String id = "" + company.getId();
		String name = company.getName();
		return new CompanyDTO(id, name);
	}
	
}
