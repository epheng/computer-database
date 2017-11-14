package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mapper.CompanyMapper;
import model.Company;

@Component
public class CompanyDAO {

	@Autowired
	private DatabaseConnection dbconn;

	@Autowired
	CompanyMapper mapper;

	private String listQuery = "SELECT * FROM company";
	private String getByIdQuery = "SELECT * FROM company WHERE id = ?";
	private String getIdByNameQuery = "SELECT id FROM company WHERE name = ?";
	private String deleteQuery = "DELETE FROM company WHERE id = ?";

	public List<Company> listAllCompanies() {
		List<Company> companyList = null;
		try(Connection conn = dbconn.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(listQuery)) {
			ResultSet rs = prepStmt.executeQuery();
			companyList = new ArrayList<Company>();
			while(rs.next()) {
				companyList.add(mapper.toEntity(rs));
			}
		}
		catch(SQLException e) {
			// TODO
			e.printStackTrace();
		}
		return companyList;
	}
	
	public Company getCompanyById(int id) {
		Company company = null;
		try(Connection conn = dbconn.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(getByIdQuery)) {
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
				company = mapper.toEntity(rs);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return company;
	}
	
	public int getCompanyIdByName(String name) {
		int id = 0;
		try(Connection conn = dbconn.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(getIdByNameQuery)) {
			prepStmt.setString(1, name);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next())
				id = rs.getInt("id");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public void deleteCompanyById(int id, Connection conn) throws SQLException {
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(deleteQuery);
			prepStmt.setInt(1, id);
			prepStmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
			conn.rollback();
		}
		finally {
			conn.setAutoCommit(true);
			prepStmt.close();
		}
	}

}
