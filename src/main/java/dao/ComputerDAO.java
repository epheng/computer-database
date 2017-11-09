package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mapper.ComputerMapper;
import model.Computer;
import service.DatabaseConnection;

public class ComputerDAO {

	private static ComputerDAO instance = null;

	public Connection conn = DatabaseConnection.getInstance();
	public ComputerMapper mapper = ComputerMapper.getInstance();

	private String listQuery = "SELECT * FROM computer";
	private String findQuery = "SELECT * FROM computer WHERE id = ?";
	private String findSomeQuery = "SELECT * FROM computer LIMIT ? OFFSET ?";
	private String findByNameOrCompanyQuery = "SELECT * FROM computer JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";
	private String deleteQuery = "DELETE FROM computer WHERE id = ?";
	private String deleteByCompanyQuery = "DELETE FROM computer WHERE company_id = ?";
	private String countQuery = "SELECT COUNT(*) AS count FROM computer";
	private String createQuery = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
	private String updateQuery = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

	private ComputerDAO() {}

	public static ComputerDAO getInstance() {
		if(instance == null) {
			instance = new ComputerDAO();
		}
		return instance;
	}

	public List<Computer> listAllComputers() {
		List<Computer> computerList = null;
		try(PreparedStatement prepStmt = conn.prepareStatement(listQuery)) {
			ResultSet rs = prepStmt.executeQuery();
			computerList = new ArrayList<Computer>();
			while(rs.next()) {
				computerList.add(mapper.toEntity(rs));
			}
		}
		catch(SQLException e) {
			// TODO
			e.printStackTrace();
		}
		return computerList;
	}
	
	public List<Computer> findComputers(int firstId, int nbComputerPerPage) {
		List<Computer> computerList = null;
		try(PreparedStatement prepStmt = conn.prepareStatement(findSomeQuery)) {
			prepStmt.setInt(1, nbComputerPerPage);
			prepStmt.setInt(2, firstId - 1);
			ResultSet rs = prepStmt.executeQuery();
			computerList = new ArrayList<Computer>();
			while(rs.next()) {
				computerList.add(mapper.toEntity(rs));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return computerList;
	}

	public Computer findComputerById(int id) {
		Computer computer = null;
		try(PreparedStatement prepStmt = conn.prepareStatement(findQuery)) {
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()) {
				computer = mapper.toEntity(rs);
			}
		}
		catch(SQLException e) {
			// TODO
			e.printStackTrace();
		}
		return computer;
	}
	
	public List<Computer> findComputersByNameOrCompany(String match) {
		List<Computer> computerList = null;
		try(PreparedStatement prepStmt = conn.prepareStatement(findByNameOrCompanyQuery)) {
			prepStmt.setString(1, "%" + match + "%");
			prepStmt.setString(2, "%" + match + "%");
			ResultSet rs = prepStmt.executeQuery();
			computerList = new ArrayList<Computer>();
			while(rs.next()) {
				computerList.add(mapper.toEntity(rs));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return computerList;
	}
	
	public int countComputers() {
		int count = 0;
		try(PreparedStatement prepStmt = conn.prepareStatement(countQuery)) {
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
				count = rs.getInt("count");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void deleteComputerById(int id) {
		try(PreparedStatement prepStmt = conn.prepareStatement(deleteQuery)) {
			prepStmt.setInt(1, id);
			prepStmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
	}
	
	public void deleteComputersByCompanyId(int id) {
		try(PreparedStatement prepStmt = conn.prepareStatement(deleteByCompanyQuery)) {
			prepStmt.setInt(1, id);
			prepStmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
	}

	public void createComputer(String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		try(PreparedStatement prepStmt = conn.prepareStatement(createQuery)) {
			prepStmt.setString(1, name);
			prepStmt.setTimestamp(2, introduced);
			prepStmt.setTimestamp(3, discontinued);
			prepStmt.setInt(4, companyId);
			prepStmt.executeUpdate();
		}
		catch(SQLException e) {
			// TODO
			e.printStackTrace();			
		}
	}

	public void updateComputerById(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		try(PreparedStatement prepUpdateStmt = conn.prepareStatement(updateQuery)) {
			Computer originalComputer = findComputerById(id);
			String updatedName = name.isEmpty() ? originalComputer.getName() : name;
			
			Timestamp updatedIntroduced = introduced == null ? originalComputer.getIntroduced() : introduced;
			Timestamp updatedDiscontinued = discontinued == null ? originalComputer.getDiscontinued() : discontinued;
					
			int updatedCompanyId = companyId == 0 ? originalComputer.getCompanyId() : companyId;
			prepUpdateStmt.setString(1, updatedName);
			prepUpdateStmt.setTimestamp(2, updatedIntroduced);
			prepUpdateStmt.setTimestamp(3, updatedDiscontinued);
			prepUpdateStmt.setInt(4, updatedCompanyId);
			prepUpdateStmt.setInt(5, id);
			prepUpdateStmt.executeUpdate();
		}
		catch(SQLException e) {
			// TODO
			e.printStackTrace();
		}
	}

}
