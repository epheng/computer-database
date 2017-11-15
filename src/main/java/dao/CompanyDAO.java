package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import mapper.CompanyMapper;
import model.Company;

@Component
public class CompanyDAO {

	@Autowired
	CompanyMapper mapper;
	
	@Autowired
	ComputerDAO computerDao;
	
	@Autowired
	private PlatformTransactionManager transManager;
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private String listQuery = "SELECT * FROM company";
	private String getByIdQuery = "SELECT * FROM company WHERE id = ?";
	private String getIdByNameQuery = "SELECT id FROM company WHERE name = ?";
	private String deleteQuery = "DELETE FROM company WHERE id = ?";
	
	private class CompanyRowMapper implements RowMapper<Company> {
		public Company mapRow(ResultSet rs, int rowNb) throws SQLException {
			return mapper.toEntity(rs);
		}
	}
	
	public List<Company> listAllCompanies() {
		return this.jdbcTemplate.query(listQuery, new CompanyRowMapper());
	}
	
	public Company getCompanyById(int id) {
		return this.jdbcTemplate.queryForObject(getByIdQuery, new Object[]{ id }, new CompanyRowMapper());
	}
	
	public int getCompanyIdByName(String name) {
		return this.jdbcTemplate.queryForObject(getIdByNameQuery, new Object[] { name }, Integer.class);
	}
	
	public void deleteCompanyById(int id) {
		DefaultTransactionDefinition transDef = new DefaultTransactionDefinition();
		TransactionStatus status = transManager.getTransaction(transDef);
		try {
			computerDao.deleteComputersByCompanyId(id);
			this.jdbcTemplate.update(deleteQuery, id);
			transManager.commit(status);
		} catch(Exception e) {
			transManager.rollback(status);	
		}
	}

}
