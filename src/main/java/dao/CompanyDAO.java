package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mapper.CompanyMapper;
import model.Company;
import service.DatabaseConnection;

public class CompanyDAO {

	private static CompanyDAO instance = null;

	public Connection conn = DatabaseConnection.getInstance();
	public CompanyMapper mapper = CompanyMapper.getInstance();

	private String listQuery = "SELECT * FROM company";
	private String getByIdQuery = "SELECT * FROM company WHERE id = ?";

	private CompanyDAO() {}

	public static CompanyDAO getInstance() {
		if(instance == null) {
			instance = new CompanyDAO();
		}
		return instance;
	}

	public List<Company> listAllCompanies() {
		List<Company> companyList = null;
		try(PreparedStatement prepStmt = conn.prepareStatement(listQuery)) {
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
		try(PreparedStatement prepStmt = conn.prepareStatement(getByIdQuery)) {
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

}
