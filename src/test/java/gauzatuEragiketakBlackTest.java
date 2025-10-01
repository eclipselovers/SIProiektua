import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;

public class gauzatuEragiketakBlackTest {
	protected  DataAccess  db = new DataAccess();
	
	public gauzatuEragiketakBlackTest() {
		db.open();
	}
	
	@Before
	void setUp() {
		db.addDriver("Proba", "123456");
		
	}


	@Test
	public void test1() {
		boolean r = false;
			
			double adirua = db.getActualMoney("Proba");
			r = db.gauzatuEragiketa("Proba", 5, true);
			if(r) {
				assertEquals(adirua+5, db.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test2() {
			boolean r1 = false;
			double adirua = db.getActualMoney("Proba");
			r1 = db.gauzatuEragiketa("Proba", 5, false);
			if(r1) {
				assertEquals(adirua-5,db.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test3() {
			boolean r2 = false;
			
			double adirua = db.getActualMoney("Proba");
			r2 = db.gauzatuEragiketa("Proba", adirua+5, false);
			if(r2) {
				assertEquals(0,db.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
			
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			r = db.gauzatuEragiketa(null, 5, false);
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		try {
			boolean r = false;
			r = db.gauzatuEragiketa("admin", 5, true);
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@After
	void tearDown() {
		db.close();
	}

}