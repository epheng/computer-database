package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import config.ConfigCDB;
import dto.CompanyDTO;
import dto.ComputerDTO;
import mapper.CompanyMapper;
import mapper.ComputerMapper;
import model.Company;
import model.Computer;
import service.DatabaseService;

@Controller
public class DashboardServlet extends HttpServlet {
	
	@Autowired
	ComputerMapper mapper;
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	DatabaseService service;
	
	int nbComputerPerPage = 10;
	int nbPage = 1;
	
	public void init() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigCDB.class);
		context.getAutowireCapableBeanFactory().autowireBean(this);
	}
	
	public List<ComputerDTO> initComputerDtoList(List<Computer> computerList) {
		List<ComputerDTO> computerDtoList = null;
		computerDtoList = new ArrayList<ComputerDTO>();
		for(Computer c : computerList) {
			computerDtoList.add(mapper.toComputerDTO(c));
		}
		return computerDtoList;
	}
	
	public List<CompanyDTO> initCompanyDtoList(List<Company> companyList) {
		List<CompanyDTO> companyDtoList = null;
		companyDtoList = new ArrayList<CompanyDTO>();
		for(Company c : companyList) {
			companyDtoList.add(companyMapper.toCompanyDTO(c));
		}
		return companyDtoList;
	}
	
	public String getUri(HttpServletRequest request) {
		String uri = request.getRequestURI().toString();
		String[] parts = uri.split("/");
		return parts[2];
	}
	
	public void setPagination(HttpServletRequest request) {
		nbPage = request.getParameter("page") == null ? nbPage : Integer.parseInt(request.getParameter("page"));
		nbComputerPerPage = request.getParameter("length") == null ? nbComputerPerPage : Integer.parseInt(request.getParameter("length"));
	}
	
	public void dashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setPagination(request);
		List<Computer> computerList = service.findComputers(nbPage, nbComputerPerPage);
		List<ComputerDTO> computerDtoList = initComputerDtoList(computerList);
		int totalNbComputers = service.countComputers();		
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		
		request.setAttribute("companies", companyDtoList);		
		request.setAttribute("computerDtoList", computerDtoList);
		request.setAttribute("nbComputer", totalNbComputers);
		request.setAttribute("currentNbPage", nbPage);
		request.setAttribute("nbPagination", totalNbComputers % nbComputerPerPage == 0 ? totalNbComputers / nbComputerPerPage : totalNbComputers / nbComputerPerPage + 1);
		
		RequestDispatcher view = request.getRequestDispatcher("views/dashboard.jsp");
		view.forward(request, response);
	}
	
	public void editComputer(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Computer computer = service.findComputerById(id);
		ComputerDTO computerDto = mapper.toComputerDTO(computer);
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		
		request.setAttribute("computer", computerDto);
		request.setAttribute("companies", companyDtoList);
		
		RequestDispatcher view = request.getRequestDispatcher("views/editComputer.jsp");
		view.forward(request, response);
	}
	
	public void addComputer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		
		request.setAttribute("companies", companyDtoList);
		RequestDispatcher view = request.getRequestDispatcher("views/addComputer.jsp");
		view.forward(request, response);
	}
	
	public void createComputer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		service.addComputer(request);
		response.sendRedirect(request.getContextPath() + "/dashboard");
	}
	
	public void updateComputer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		service.updateComputer(request);
		response.sendRedirect(request.getContextPath() + "/dashboard");
	}
	
	public void deleteComputers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service.deleteComputers(request);
		response.sendRedirect(request.getContextPath() + "/dashboard");
	}
	
	public void searchComputers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setPagination(request);
		List<Computer> computerList = service.searchComputersByNameOrCompany(request);
		List<ComputerDTO> computerDtoList = initComputerDtoList(computerList);
		int totalNbComputers = service.countComputers();
		
		request.setAttribute("computerDtoList", computerDtoList);
		request.setAttribute("nbComputer", totalNbComputers);
		request.setAttribute("currentNbPage", nbPage);
		request.setAttribute("nbPagination", totalNbComputers % nbComputerPerPage == 0 ? totalNbComputers / nbComputerPerPage : totalNbComputers / nbComputerPerPage + 1);
		
		RequestDispatcher view = request.getRequestDispatcher("views/dashboard.jsp");
		view.forward(request, response);
	}
	
	public void deleteCompany(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service.deleteCompany(request);
		response.sendRedirect(request.getContextPath() + "/dashboard");
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
		
		switch(getUri(request)) {
		case "dashboard":
			if(request.getParameter("action") != null) {
				if(request.getParameter("action").equals("search")) {
					searchComputers(request, response);
				}
				else if(request.getParameter("action").equals("deleteCompany")) {
					deleteCompany(request, response);
				}
			} else {
				deleteComputers(request, response);
			}
			break;
		case "editComputer":
			updateComputer(request, response);
			break;
		case "addComputer":
			createComputer(request, response);
			break;
		}
		
	}
}
