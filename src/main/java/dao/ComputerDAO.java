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
	private String deleteQuery = "DELETE FROM computer WHERE id = ?";
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

	public void deleteComputerById(int id) {
		try(PreparedStatement prepStmt = conn.prepareStatement(deleteQuery)) {
			prepStmt.setInt(1, id);
			prepStmt.executeUpdate();
		}
		catch(SQLException e) {
			// TODO
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
