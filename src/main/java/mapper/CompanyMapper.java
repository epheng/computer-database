package mapper;


import org.springframework.stereotype.Component;

import dto.CompanyDTO;
import model.Company;

@Component
public class CompanyMapper {
	
	public CompanyDTO toCompanyDTO(Company company) {
		String id = "" + company.getId();
		String name = company.getName();
		return new CompanyDTO(id, name);
	}
	
}
