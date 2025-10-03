import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class BookRideBDBWhiteTest {

	protected  EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public BookRideBDBWhiteTest() {
		db.open();
		et.getTransaction().begin();
		Traveler traveler = new Traveler("Proba", "123456");
		traveler.setMoney(1000);
		et.merge(traveler);
		et.getTransaction().commit();
		db.close();
	}
	
	

	@Test
	public void test1() {
		boolean r = false;
		Driver d = new Driver("Proba", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		db.open();
		r = db.bookRide(null, ride, 1, 1.1);
		db.close();
		assertFalse(r);
	}

	@Test
	public void test2() {
		boolean r2 = false;
		db.open();
		r2 = db.bookRide("Proba", null, 1, 1.1);
		db.close();
		assertFalse(r2);
	}
	
	@Test
	public void test3() {
		Driver d = new Driver("Proba", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r3 = false;
		db.open();
		r3 = db.bookRide("Proba", ride, 3, 1.1);
		db.close();
		assertFalse(r3);
	}
	
	@Test
	public void test4() {
		Driver d = new Driver("Proba", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		boolean r3 = false;
		db.open();
		r3 = db.bookRide("Proba", ride, 1, 1.1);
		db.close();
		assertFalse(r3);
	}
}
