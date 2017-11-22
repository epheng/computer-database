package service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.CompanyDAO;
import dao.ComputerDAO;
import dao.DatabaseConnection;
import model.Company;
import model.Computer;

@Component
public class DatabaseService {
	
	@Autowired
	DatabaseConnection dbconn;
	
	@Autowired
	ComputerDAO computerDao;
	
	@Autowired
	CompanyDAO companyDao;
	
	public void addComputer(Computer computer) {
		String computerName = computer.getName();
		Timestamp introducedDate = computer.getIntroduced();
		Timestamp discontinuedDate = computer.getDiscontinued();
		int companyId = computer.getCompanyId();
		computerDao.createComputer(computerName, introducedDate, discontinuedDate, companyId);
	}
	
	public int getCompanyIdbyName(String name) {
		return companyDao.getCompanyIdByName(name);
	}
	
	public Computer findComputerById(int id) {
		return computerDao.findComputerById(id);
	}
	
	public List<Computer> findComputers(int pageNumber, int nbComputerPerPage) {
		int firstId = (pageNumber-1) * nbComputerPerPage + 1;
		return computerDao.findComputers(firstId, nbComputerPerPage);
	}
	
	public int countComputers() {
		return computerDao.countComputers();
	}
	
	public void updateComputer(Computer computer) {
		int computerId = computer.getId();
		Computer originalComputer = findComputerById(computerId);
		
		String computerName = computer.getName();
		Timestamp introducedDate = computer.getIntroduced();
		Timestamp discontinuedDate = computer.getDiscontinued();
		int companyId = computer.getCompanyId();
		
		String updatedName = computerName.isEmpty() ? originalComputer.getName() : computerName;
		Timestamp updatedIntroduced = introducedDate == null ? originalComputer.getIntroduced() : introducedDate;
		Timestamp updatedDiscontinued = discontinuedDate == null ? originalComputer.getDiscontinued() : discontinuedDate;
		int updatedCompanyId = companyId == 0 ? originalComputer.getCompanyId() : companyId;
		
		computerDao.updateComputerById(computerId, updatedName, updatedIntroduced, updatedDiscontinued, updatedCompanyId);
	}
	
	public void deleteComputerById(int id) {
		computerDao.deleteComputerById(id);
	}
	
	public void deleteComputers(String selection) {
		String[] ids = selection.split(",");
		for(String id : ids) {
			deleteComputerById(Integer.parseInt(id));
		}
	}
	
	public List<Company> findAllCompanies() {
		return companyDao.listAllCompanies();
	}
	
	public List<Computer> searchComputersByNameOrCompany(String match) {
		return computerDao.findComputersByNameOrCompany(match);
	}
	
	public void deleteCompany(String selectionCompany) {
		int companyId = Integer.parseInt(selectionCompany);
		companyDao.deleteCompanyById(companyId);
	}
	
}
