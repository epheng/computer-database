package dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

@Component
public class ComputerDTO {
	
	private String id;
	
	@Size(min = 2, max = 20)
	private String name;
	
	@Pattern(regexp = "^(?:(0[1-9]|[12]\\d|3[01])(\\/|-)(0[1-9]|1[012])(\\/|-)\\d\\d(\\d\\d)?|\\d\\d\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])|)$",
			message = "Enter a valid date")
	private String introduced;
	
	@Pattern(regexp = "^(?:(0[1-9]|[12]\\d|3[01])(\\/|-)(0[1-9]|1[012])(\\/|-)\\d\\d(\\d\\d)?|\\d\\d\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12]\\d|3[01])|)$",
			message = "Enter a valid date")
	private String discontinued;
	
	@NotNull
	private String company;
	
	public ComputerDTO() {}
	
	public ComputerDTO(String id, String name, String introduced, String discontinued, String company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
}
