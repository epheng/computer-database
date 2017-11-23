package dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.Computer;

@Repository
public class ComputerDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	private final String findQuery = "FROM Computer WHERE id = :id";
	private final String findSomeQuery = "FROM Computer";
	private final String findByNameOrCompanyQuery = "FROM Computer WHERE name LIKE :name";
	private final String deleteByCompanyQuery = "DELETE Computer WHERE company_id = :id";
	private final String countQuery = "SELECT COUNT(*) FROM Computer";
	
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
		List<Computer> list = null;
		try(Session session = sessionFactory.openSession();) {
			Query<Computer> query = session.createQuery(findByNameOrCompanyQuery);
			query.setParameter("name", "%" + match + "%");
			list = query.list();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return list;
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
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			Computer computer = new Computer();
			computer.setId(id);
			session.delete(computer);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
		}
	}

	public void deleteComputersByCompanyId(int id) {
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			Query<Computer> query = session.createQuery(deleteByCompanyQuery);
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
		}
	}
	
	public void createComputer(String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			Computer computer = new Computer(name, introduced, discontinued, companyId);
			session.save(computer);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
		}
	}
	
	public void updateComputerById(int id, String name, Timestamp introduced, Timestamp discontinued, int companyId) {
		Transaction tx = null;
		try(Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			Computer computer = new Computer(id, name, introduced, discontinued, companyId);
			session.update(computer);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
		}
	}

}
