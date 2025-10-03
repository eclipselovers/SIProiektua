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
	      db.addTraveler("Proba", "22456");
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
	    boolean r2 = db.bookRide("Proba", null, 1, 1.1);
	    db.close();
	    assertFalse(r2);  
	}


	
	@Test
	public void test3() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r3 = false;
		db.open();
		Traveler t = db.getTraveler("Proba");
		t.setMoney(0);
		db.updateTraveler(t);
		r3 = db.bookRide("Proba", ride, 1, 1.1);
		db.close();
		assertFalse(r3);
	}
	
	@Test
	public void test4() {
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r4 = false;
		db.open();
		r4 = db.bookRide("Proba", ride, 45, 1.1);
		db.close();
		assertFalse(r4);
	}
	
	@Test
	public void test5() {
		db.open();
		Traveler t = db.getTraveler("Proba");
		t.setMoney(1000*0);
		db.updateTraveler(t);
	    db.close();
	    Driver d = new Driver("a", "struy54");
	    Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
	    boolean r5 = false;
	    db.open();
	    r5 = db.bookRide("Proba", ride, 1, 1.1);
	    db.close();
	    assertFalse(r5);
	}

}
