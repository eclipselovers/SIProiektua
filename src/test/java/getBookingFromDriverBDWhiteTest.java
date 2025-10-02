import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Ride;

public class getBookingFromDriverBDWhiteTest {
	protected  DataAccess  db = new DataAccess();
	
	public getBookingFromDriverBDWhiteTest() {
		db.open();
	}
	@Before
	public void setUp() {
		db.addDriver("Driver", "123");
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
}
