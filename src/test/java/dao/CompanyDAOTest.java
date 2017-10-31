package dao;

import static org.junit.Assert.assertNotNull;

import org.junit.*;

public class CompanyDAOTest {

	@Test
	public void listAllCompaniesTest() {
		CompanyDAO companyDao = CompanyDAO.getInstance();
		
		assertNotNull("list not null", companyDao.listAllCompanies());
	}

}
