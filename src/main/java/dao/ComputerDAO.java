package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
	
	@Autowired
	private SessionFactory sessionFactory;

//	private String findQuery = "SELECT * FROM computer WHERE id = ?";
	private String findQuery = "FROM Computer WHERE id = :id";
//	private String findSomeQuery = "SELECT * FROM computer LIMIT ? OFFSET ?";
	private String findSomeQuery = "FROM Computer";
	private String findByNameOrCompanyQuery = "SELECT * FROM computer JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";
//	private String deleteQuery = "DELETE FROM computer WHERE id = ?";
	private String deleteQuery = "DELETE Computer WHERE id = :id";
//	private String deleteByCompanyQuery = "DELETE FROM computer WHERE company_id = ?";
	private String deleteByCompanyQuery = "DELETE Computer WHERE company_id = :id";
//	private String countQuery = "SELECT COUNT(*) AS count FROM computer";
	private String countQuery = "SELECT COUNT(*) FROM Computer";
	private String createQuery = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
	private String updateQuery = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	
	private class ComputerRowMapper implements RowMapper<Computer> {
		public Computer mapRow(ResultSet rs, int rowNb) throws SQLException {
			return mapper.toEntity(rs);
		}
	}
	
	public List<Computer> findComputers(int firstId, int nbComputerPerPage) {
		List<Computer> list = null;
		try(Session session = sessionFactory.openSession();) {
			Query<Computer> query = session.createQuery(findSomeQuery);
			query.setFirstResult(firstId - 1);
			query.setMaxResults(nbComputerPerPage);
			list = query.list();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Computer findComputerById(int id) {
		Computer computer = null;
		try(Session session = sessionFactory.openSession();) {
			Query<Computer> query = session.createQuery(findQuery);
			query.setParameter("id", id);
			computer = query.uniqueResult();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return computer;
	}
	
	public List<Computer> findComputersByNameOrCompany(String match) {
		return this.jdbcTemplate.query(findByNameOrCompanyQuery, new Object[] { "%" + match + "%", "%" + match + "%" }, new ComputerRowMapper());
	}
	
	public int countComputers() {
		Integer count = 0;
		try(Session session = sessionFactory.openSession();) {
			Query<Integer> query = session.createQuery(countQuery);
			count = ((Number) query.uniqueResult()).intValue();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public void deleteComputerById(int id) {
//		this.jdbcTemplate.update(deleteQuery, id);
		try(Session session = sessionFactory.openSession();) {
			Transaction tx = session.beginTransaction();
			Query<Computer> query = session.createQuery(deleteQuery);
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
	}

	public void deleteComputersByCompanyId(int id) {
//		this.jdbcTemplate.update(deleteByCompanyQuery, id);
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			Query<Computer> query = session.createQuery(deleteByCompanyQuery);
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		}
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
