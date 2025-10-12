import static org.junit.Assert.assertFalse;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;


import org.junit.Test;

import dataAccess.DataAccess;

public class gauzatuEragiketakWhiteTest {
	protected  EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public gauzatuEragiketakWhiteTest() {
		db.open();
		db.addUser("Proba", "123456", domain.Driver.class);
		db.close();
	}
	
	

	@Test
	public void test1() {
		boolean r = false;
		db.open();
		r = db.gauzatuEragiketa(null, 0, true);
		db.close();
		assertFalse(r);
	}
	
	@Test
	public void test2() {
		try {
			boolean r1 = false;
			db.open();
			r1 = db.gauzatuEragiketa("Proba", 0, false);
			db.close();
			assertTrue(r1);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		try {
			boolean r2 = false;
			db.open();
			r2 = db.gauzatuEragiketa("Proba", 0, true);
			db.close();
			assertTrue(r2);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			db.open();
			r = db.gauzatuEragiketa("Proba", 1000, false);
			db.close();
			assertTrue(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
}