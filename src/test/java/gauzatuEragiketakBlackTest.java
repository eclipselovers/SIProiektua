import static org.junit.Assert.assertEquals;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;

import dataAccess.DataAccess;

public class gauzatuEragiketakBlackTest {
	protected  EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public gauzatuEragiketakBlackTest() {
		db.open();
		db.addDriver("Proba", "123456");
		db.close();

	}
	


	@Test
	public void test1() {
		boolean r = false;
			db.open();
			double adirua = db.getActualMoney("Proba");
			r = db.gauzatuEragiketa("Proba", 5, true);
			db.close();
			if(r) {
				db.open();
				assertEquals(adirua+5, db.getActualMoney("Proba"), 0.0001);
				db.close();
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test2() {
			boolean r1 = false;
			db.open();
			double adirua = db.getActualMoney("Proba");
			r1 = db.gauzatuEragiketa("Proba", 5, false);
			db.close();
			if(r1) {
				db.open();
				assertEquals(adirua-5,db.getActualMoney("Proba"), 0.0001);
				db.close();
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test3() {
			boolean r2 = false;
			db.open();
			double adirua = db.getActualMoney("Proba");
			r2 = db.gauzatuEragiketa("Proba", adirua+5, false);
			db.close();
			if(r2) {
				db.open();
				assertEquals(0,db.getActualMoney("Proba"), 0.0001);
				db.close();
			} else {
				fail();
			}
			
			
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			db.open();
			r = db.gauzatuEragiketa(null, 5, false);
			db.close();
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		try {
			boolean r = false;
			db.open();
			r = db.gauzatuEragiketa("admin", 5, true);
			db.close();
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	

}