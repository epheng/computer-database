package service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dao.CompanyDAO;
import dao.ComputerDAO;
import model.Company;
import model.Computer;

public class DatabaseService {
	
	public ComputerDAO computerDao = ComputerDAO.getInstance();
	public CompanyDAO companyDao = CompanyDAO.getInstance();
	
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
	
	public void addComputer(HttpServletRequest request) {
		String name = request.getParameter("computerName");
		Timestamp introduced = parseTimestamp(request.getParameter("introduced"));
		Timestamp introducedDate = introduced == null ? null : introduced;
		Timestamp discontinued = parseTimestamp(request.getParameter("discontinued"));
		Timestamp discontinuedDate = discontinued == null ? null : discontinued;
		String company = request.getParameter("companyId");
		int companyId = getCompanyIdbyName(company);
		computerDao.createComputer(name, introducedDate, discontinuedDate, companyId);
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
	
	public void updateComputer(HttpServletRequest request) {
		int computerId = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("computerName");
		Timestamp introduced = parseTimestamp(request.getParameter("introduced"));
		Timestamp introducedDate = introduced == null ? null : introduced;
		Timestamp discontinued = parseTimestamp(request.getParameter("discontinued"));
		Timestamp discontinuedDate = discontinued == null ? null : discontinued;
		String company = request.getParameter("companyId");
		int companyId = getCompanyIdbyName(company);
		computerDao.updateComputerById(computerId, name, introducedDate, discontinuedDate, companyId);
	}
	
	public void deleteComputerById(int id) {
		computerDao.deleteComputerById(id);
	}
	
	public void deleteComputers(HttpServletRequest request) {
		String computerIds = request.getParameter("selection");
		String[] ids = computerIds.split(",");
		for(String id : ids) {
			deleteComputerById(Integer.parseInt(id));
		}
	}
	
	public List<Company> findAllCompanies() {
		return companyDao.listAllCompanies();
	}
	
	public List<Computer> searchComputersByNameOrCompany(HttpServletRequest request) {
		String match = request.getParameter("search");
		return computerDao.findComputersByNameOrCompany(match);
	}
	
	public void deleteCompany(HttpServletRequest request) {
		int companyId = Integer.parseInt(request.getParameter("selectionCompany"));
		computerDao.deleteComputersByCompanyId(companyId);
		companyDao.deleteCompanyById(companyId);
	}
	
}
