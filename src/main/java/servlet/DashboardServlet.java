package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CompanyDAO;
import dao.ComputerDAO;
import dto.CompanyDTO;
import dto.ComputerDTO;
import mapper.ComputerMapper;
import model.Company;
import model.Computer;

public class DashboardServlet extends HttpServlet {
	
	ComputerMapper mapper = ComputerMapper.getInstance();
	List<ComputerDTO> computerDtoList = null;
	List<CompanyDTO> companyDtoList = null;
	
	public void initComputerDtoList(List<Computer> computerList) {
		computerDtoList = new ArrayList<ComputerDTO>();
		for(Computer c : computerList) {
			computerDtoList.add(mapper.toComputerDTO(c));
		}
	}
	
	public void initCompanyDtoList(List<Company> companyList) {
		companyDtoList = new ArrayList<CompanyDTO>();
		for(Company c : companyList) {
			companyDtoList.add(mapper.toCompanyDTO(c));
		}
	}
	
	public String getUri(HttpServletRequest request) {
		String uri = request.getRequestURI().toString();
		String[] parts = uri.split("/");
		return parts[2];
	}
	
	public void dashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Computer> computerList = ComputerDAO.getInstance().listAllComputers();
		initComputerDtoList(computerList);
		
		request.setAttribute("computerDtoList", computerDtoList);
		request.setAttribute("nbComputer", computerDtoList.size());
		
		RequestDispatcher view = request.getRequestDispatcher("views/dashboard.jsp");
		view.forward(request, response);
	}
	
	public void editComputer(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Computer computer = ComputerDAO.getInstance().findComputerById(id);
		ComputerDTO computerDto = mapper.toComputerDTO(computer);
		List<Company> companyList = CompanyDAO.getInstance().listAllCompanies();
		initCompanyDtoList(companyList);
		
		request.setAttribute("computer", computerDto);
		request.setAttribute("companies", companyList);
		
		RequestDispatcher view = request.getRequestDispatcher("views/editComputer.jsp");
		view.forward(request, response);
	}
	
	public void addComputer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("views/addComputer.jsp");
		view.forward(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		
		switch(getUri(request)) {
		case "dashboard":
			dashboard(request, response);
			break;
		case "editComputer":
			editComputer(request, response);
			break;
		case "addComputer":
			addComputer(request, response);
			break;
		}
			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("computerName");
		String introduced = request.getParameter("introduced");
		String discontinued = request.getParameter("discontinued");
		String company = request.getParameter("companyId");
		System.out.println(name);
		System.out.println(introduced);
		System.out.println(discontinued);
		System.out.println(company);		
	}
}
