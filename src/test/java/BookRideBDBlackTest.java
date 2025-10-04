import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class BookRideBDBlackTest {
	protected EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public BookRideBDBlackTest() {
	      db.open();
	      db.addTraveler("Traveler Test", "22456");
	      db.close();
	}
	
	

	@Test
	public void test1() {
		boolean r = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		db.open();
		r = db.bookRide(null, ride, 1, 1.1);
		db.close();
		assertFalse(r);
	}
	
	@Test
	public void test2() {
	    db.open();
	    boolean r2 = db.bookRide("Traveler Test", null, 1, 1.1);
	    db.close();
	    assertFalse(r2);  
	}


	
	@Test
	public void test3() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		try {
			db.open();
			db.bookRide("Traveler Test", ride, -1, 1.1);
			db.close();
			fail();
		} catch (Exception e) {
			assertFalse(false);
		}
	
	}
	
	@Test
	public void test4() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r4 = false;
		db.open();
		r4 = db.bookRide("Traveler Test", ride, 45, 1.1);
		db.close();
		assertFalse(r4);
	}
	
	@Test
	public void test5() {
		db.open();
		Traveler t = db.getTraveler("Traveler Test");
		t.setMoney(1000);
	    db.close();
	    Driver d = new Driver("a", "struy54");
	    Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
	    boolean r5 = false;
	    db.open();
	    r5 = db.bookRide("Traveler Test", ride, 1, 1.1);
	    db.close();
	    assertFalse(r5);
	}
	
	@Test
	public void test6() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r4 = false;
		db.open();
		r4 = db.bookRide("Test", ride, 1, 1.1);
		db.close();
		assertFalse(r4);
	}
	
	@Test
	public void test7() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r4 = false;
		try {
			db.open();
			r4 = db.bookRide("Traveler Test", ride, 1, -1.1);
			db.close();
			assertFalse(r4);	
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}

}
