package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="computer")
public class Computer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="introduced")
	private Timestamp introduced;
	
	@Column(name="discontinued")
	private Timestamp discontinued;
	
	@Column(name="company_id")
	private Integer companyId;
	
	public Computer() {}
	
	public Computer(String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}
	
	public Computer(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Timestamp getIntroduced() {
		return introduced;
	}
	
	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}
	
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	
	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}
	
	public int getCompanyId() {
		return companyId == null ? 0 : companyId;
	}
	
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	public String toString() {
		return "Name : " + name +
				"\nIntroduced date : " + introduced +
				"\nDiscontinued date : " + discontinued +
				"\nCompany id : " + companyId;
	}
	
}
