package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import service.DatabaseService;

public class DashboardServlet extends HttpServlet {
	
	ComputerMapper mapper = ComputerMapper.getInstance();
	DatabaseService service = new DatabaseService();
	List<ComputerDTO> computerDtoList = null;
	List<CompanyDTO> companyDtoList = null;
	List<Company> companyList = null;
	int nbComputerPerPage = 10;
	int nbPage = 1;
	
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
//		List<Computer> computerList = ComputerDAO.getInstance().listAllComputers();
		nbPage = request.getParameter("page") == null ? nbPage : Integer.parseInt(request.getParameter("page"));
		nbComputerPerPage = request.getParameter("length") == null ? nbComputerPerPage : Integer.parseInt(request.getParameter("length"));
		List<Computer> computerList = service.findComputers(nbPage, nbComputerPerPage);
		initComputerDtoList(computerList);
		int totalNbComputers = service.countComputers();
		
		request.setAttribute("computerDtoList", computerDtoList);
		request.setAttribute("nbComputer", totalNbComputers);
		request.setAttribute("currentNbPage", nbPage);
		request.setAttribute("currentNbComputerPerPage", nbComputerPerPage);
		request.setAttribute("nbPagination", totalNbComputers % nbComputerPerPage == 0 ? totalNbComputers / nbComputerPerPage : totalNbComputers / nbComputerPerPage + 1);
		
		RequestDispatcher view = request.getRequestDispatcher("views/dashboard.jsp");
		view.forward(request, response);
	}
	
	public void editComputer(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Computer computer = ComputerDAO.getInstance().findComputerById(id);
		ComputerDTO computerDto = mapper.toComputerDTO(computer);
		companyList = CompanyDAO.getInstance().listAllCompanies();
		initCompanyDtoList(companyList);
		
		request.setAttribute("computer", computerDto);
		request.setAttribute("companies", companyList);
		
		RequestDispatcher view = request.getRequestDispatcher("views/editComputer.jsp");
		view.forward(request, response);
	}
	
	public void addComputer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		companyList = CompanyDAO.getInstance().listAllCompanies();
		initCompanyDtoList(companyList);
		
		request.setAttribute("companies", companyList);
		RequestDispatcher view = request.getRequestDispatcher("views/addComputer.jsp");
		view.forward(request, response);
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
	
	public void createComputer(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String name = request.getParameter("computerName");
		if(name.equals("")) {
			request.setAttribute("emptyName", "Name can't be empty");
			request.setAttribute("companies", companyList);
			request.getRequestDispatcher("views/addComputer.jsp").forward(request, response);
			return;
		}
		Timestamp introduced = parseTimestamp(request.getParameter("introduced"));
		Timestamp introducedDate = introduced == null ? null : introduced;
		Timestamp discontinued = parseTimestamp(request.getParameter("discontinued"));
		Timestamp discontinuedDate = discontinued == null ? null : discontinued;
		String company = request.getParameter("companyId");
		int companyId = service.getCompanyIdbyName(company);
		service.addComputer(name, introducedDate, discontinuedDate, companyId);
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
		case "editComputer":
//			editComputer(request, response);
			break;
		case "addComputer":
			createComputer(request, response);
			break;
		}
		
	}
}
