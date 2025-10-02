import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class getBookingFromDriverBDWhiteTest {
	protected  EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
	
	public getBookingFromDriverBDWhiteTest() {
		db.open();
	}
	@Before
	public void setUp() {
		db.addDriver("Driver", "123");
		db.addDriver("Driver2", "123");
		Driver driver2 = db.getDriver("Driver2");
		Ride rides = new Ride ("a","a", new Date(2025,10,20) ,231,2,driver2);
		rides.setActive(false);
		db.addDriver("Driver3", "123");
		Driver driver3 = db.getDriver("Driver3");
		driver3.addRide("aas", "wefef", new Date(2025,2,20), 5, 5);
		Traveler traveler = new Traveler("a", "a");
		db.bookRide("a", rides, 2, 2);
	}
	
	@After
	public void tearDown() {
		db.close();
	}
	@Test
	public void test1() {
		assertEquals(db.getBookingFromDriver(null), null);
	}
	@Test
	public void test2() {
		List<Booking> bookings = new ArrayList<>();
		assertEquals(db.getBookingFromDriver("Driver"), bookings);
	}
	@Test
	public void test3() {
		assertEquals(db.getBookingFromDriver("Driver2"), null);
	}
	@Test
	public void test4() {
		List<Booking> bookings = new ArrayList<>();
		Driver driver3 = db.getDriver("Driver3");
		List<Ride> rides  = driver3.getCreatedRides();
		for (Ride ride : rides) {
			bookings.addAll(ride.getBookings());} 
		assertEquals(db.getBookingFromDriver("Driver3"), bookings);
	}
}
