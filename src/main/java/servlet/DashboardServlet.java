package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ComputerDAO;

public class DashboardServlet extends HttpServlet {
	
	ComputerDAO computerDao = ComputerDAO.getInstance();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		
		RequestDispatcher view = request.getRequestDispatcher("views/dashboard.html");
		view.forward(request, response);
			
	}
}
