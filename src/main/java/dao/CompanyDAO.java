package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mapper.CompanyMapper;
import model.Company;

@Component
public class CompanyDAO {

	@Autowired
	CompanyMapper mapper;
	
	@Autowired
	ComputerDAO computerDao;
	/*
	@Autowired
	private PlatformTransactionManager transManager;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	*/
	@Autowired
	private SessionFactory sessionFactory;

	private String listQuery = "FROM Company";
	private String getByIdQuery = "FROM Company WHERE id = :id";
	private String getIdByNameQuery = "SELECT C.id FROM Company C WHERE C.name = :name";
	private String deleteQuery = "DELETE Company WHERE id = :id";
	
	public List<Company> listAllCompanies() {
		List<Company> list = null;
		try(Session session = sessionFactory.openSession();) {
			Query<Company> query = session.createQuery(listQuery);
			list = query.list();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Company getCompanyById(int id) {
		Company company = null;
		try(Session session = sessionFactory.openSession();) {
			Query<Company> query = session.createQuery(getByIdQuery);
			query.setParameter("id", id);
			company = query.uniqueResult();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return company;
	}
	
	public int getCompanyIdByName(String name) {
		int id = 0;
		try(Session session = sessionFactory.openSession();) {
			Query<Integer> query = session.createQuery(getIdByNameQuery);
			query.setParameter("name", name);
			id = query.uniqueResult();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public void deleteCompanyById(int id) {
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			computerDao.deleteComputersByCompanyId(id);
//			this.jdbcTemplate.update(deleteQuery, id);
			Query<Company> query = session.createQuery(deleteQuery);
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		}
	}

}
