package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Company;

@Component
public class CompanyDAO {
	
	@Autowired
	private ComputerDAO computerDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	private final String listQuery = "FROM Company";
	private final String getByIdQuery = "FROM Company WHERE id = :id";
	private final String getIdByNameQuery = "SELECT C.id FROM Company C WHERE C.name = :name";
	
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
			Company company = new Company();
			company.setId(id);
			session.delete(company);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
		}
	}

}
