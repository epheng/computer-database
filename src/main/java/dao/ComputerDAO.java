package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mapper.ComputerMapper;
import model.Computer;

@Repository
public class ComputerDAO {

	@Autowired
	private ComputerMapper mapper;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private String findQuery = "SELECT * FROM computer WHERE id = ?";
	private String findSomeQuery = "SELECT * FROM computer LIMIT ? OFFSET ?";
	private String findByNameOrCompanyQuery = "SELECT * FROM computer JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";
	private String deleteQuery = "DELETE FROM computer WHERE id = ?";
	private String deleteByCompanyQuery = "DELETE FROM computer WHERE company_id = ?";
	private String countQuery = "SELECT COUNT(*) AS count FROM computer";
	private String createQuery = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
	private String updateQuery = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	
	private class ComputerRowMapper implements RowMapper<Computer> {
		public Computer mapRow(ResultSet rs, int rowNb) throws SQLException {
			return mapper.toEntity(rs);
		}
	}
	
	public List<Computer> findComputers(int firstId, int nbComputerPerPage) {
		return this.jdbcTemplate.query(findSomeQuery, new Object[] { nbComputerPerPage, firstId - 1 }, new ComputerRowMapper());
	}
	
	public Computer findComputerById(int id) {
		return this.jdbcTemplate.queryForObject(findQuery, new Object[] { id }, new ComputerRowMapper());
	}
	
	public List<Computer> findComputersByNameOrCompany(String match) {
		return this.jdbcTemplate.query(findByNameOrCompanyQuery, new Object[] { "%" + match + "%", "%" + match + "%" }, new ComputerRowMapper());
	}
	
	public int countComputers() {
		return this.jdbcTemplate.queryForObject(countQuery, Integer.class);
	}
	
	public void deleteComputerById(int id) {
		this.jdbcTemplate.update(deleteQuery, id);
	}

	public void deleteComputersByCompanyId(int id) {
		this.jdbcTemplate.update(deleteByCompanyQuery, id);
	}
	
	public void createComputer(String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		this.jdbcTemplate.update(createQuery, name, introduced, discontinued, companyId);
	}
	
	public void updateComputerById(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		Computer originalComputer = findComputerById(id);

		String updatedName = name.isEmpty() ? originalComputer.getName() : name;
		Timestamp updatedIntroduced = introduced == null ? originalComputer.getIntroduced() : introduced;
		Timestamp updatedDiscontinued = discontinued == null ? originalComputer.getDiscontinued() : discontinued;
		int updatedCompanyId = companyId == 0 ? originalComputer.getCompanyId() : companyId;

		this.jdbcTemplate.update(updateQuery, updatedName, updatedIntroduced, updatedDiscontinued, updatedCompanyId, id);
	}

}
