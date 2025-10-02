
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;

public class gauzatuEragiketakWhiteTest {
	protected  EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public gauzatuEragiketakWhiteTest() {
		db.open();
	}
	
	@Before
	public void setUp() {
		db.addDriver("Proba", "123456");
		
	}

	@Test
	public void test1() {
		boolean r = false;
		r = db.gauzatuEragiketa(null, 0, true);
		assertFalse(r);
	}
	
	@Test
	public void test2() {
		try {
			boolean r1 = false;
			r1 = db.gauzatuEragiketa("Proba", 0, false);
			assertTrue(r1);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		try {
			boolean r2 = false;
			r2 = db.gauzatuEragiketa("Proba", 0, true);
			assertTrue(r2);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			r = db.gauzatuEragiketa("Proba", 1000, false);
			assertTrue(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@After
	public void tearDown() {
		db.close();
	}

}
