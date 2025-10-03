
import domain.Driver;
import domain.Ride;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import configuration.UtilDate;
import dataAccess.DataAccess;

public class GetRidesByDriverWhiteTest {
	protected EntityManager et;
	protected  DataAccess  db = new DataAccess(et);
		
	@Test
	// Ez dago "EzUrtzi" izeneko driverra. Null bueltatu
	public void test1() {
		try {
			db.open();
			List<Ride> erantzuna = db.getRidesByDriver("EzUrtzi");
			db.close();
			assertNull(erantzuna);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	// "Urtzi" db-an dago baino ez ditu ride-ak. Null bueltatu
	public void test2() {
		try {
			db.open();
			db.addDriver("Urtzii", "123");
			
			List<Ride> erantzuna = db.getRidesByDriver("Urtzii");
			if (erantzuna.isEmpty() == true) {
				assertTrue(true);
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			Driver urtzii = db.getDriver("Urtzii");
			db.deleteUser(urtzii);
			db.close();
		}
	}
	
	@Test
	// "Urtzi" db-an dago eta Ride ez aktibo bat du.
	public void test3() {
		try {
			db.open();
			db.addDriver("Urtzii", "123");
			Calendar cal = Calendar.getInstance();
			cal.set(2026, Calendar.MAY, 20);
			Date date = UtilDate.trim(cal.getTime());
			Ride ride = db.createRide("Donosti", "Bilbao", date, 4, 5, "Urtzii");
			ride.setActive(false);

			List<Ride> erantzuna = db.getRidesByDriver("Urtzii");
			
			if (erantzuna.isEmpty() == true) {
				assertTrue(true);
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			Driver urtzii = db.getDriver("Urtzii");
			db.deleteUser(urtzii);
			db.close();
		}
	}
	
	
	
	@Test
	// "Urtzi" db-an dago eta Ride aktibo bat du.
	public void test4() {
		try {
			db.open();
			db.addDriver("Urtzii", "123");
			Calendar cal = Calendar.getInstance();
			cal.set(2026, Calendar.MAY, 20);
			Date date = UtilDate.trim(cal.getTime());
			db.createRide("Donosti", "Bilbao", date, 4, 5, "Urtzii");

			List<Ride> erantzuna = db.getRidesByDriver("Urtzii");
			
			if (erantzuna.isEmpty() == false) {
				assertTrue(true);
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			Driver urtzii = db.getDriver("Urtzii");
			db.deleteUser(urtzii);
			db.close();
		}
	}

}
