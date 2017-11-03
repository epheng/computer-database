package service;


import java.sql.SQLException;
import java.util.List;

import dao.CompanyDAO;
import dao.ComputerDAO;
import model.Company;
import model.Computer;

public class DatabaseService {
	
	public ComputerDAO computerDao = ComputerDAO.getInstance();
	public CompanyDAO companyDao = CompanyDAO.getInstance();
	private RequestData rd;
	
	public DatabaseService(RequestData rd) {
		this.rd = rd;
	}
	
	
	
	/**
	 * FOR COMMAND LINE
	 * Method that creates a statement and
	 * calls the appropriate execution method depending on the request type
	 * @throws SQLException
	 */
	public void executeRequest() {
		switch(rd.getRequestType()) {
		case LIST_COMPUTERS:
			List<Computer> computerList = computerDao.listAllComputers();
			for(Computer comp : computerList)
				System.out.println(comp.getName());
			break;
		case LIST_COMPANIES:
			companyDao.listAllCompanies();
			List<Company> companyList = companyDao.listAllCompanies();
			for(Company comp : companyList)
				System.out.println(comp.getName());
			break;
		case SHOW_COMPUTER:
			Computer computer = computerDao.findComputerById(rd.getComputerId());
			System.out.println(computer);
			break;
		case CREATE:
			computerDao.createComputer(rd.getComputer_name(),
					rd.getIntroducedDate(),
					rd.getDiscontinuedDate(),
					rd.getCompany_id());
			System.out.println("computer created.");
			break;
		case DELETE:
			computerDao.deleteComputerById(rd.getComputerId());
			System.out.println("computer deleted.");
			break;
		case UPDATE:
			computerDao.updateComputerById(rd.getComputerId(),
					rd.getComputer_name(),
					rd.getIntroducedDate(),
					rd.getDiscontinuedDate(),
					rd.getCompany_id());
			System.out.println("computer updated.");
			break;
		}
	}
	
}
