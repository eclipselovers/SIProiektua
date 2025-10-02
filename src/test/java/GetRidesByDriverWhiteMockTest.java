//Updatee
import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GetRidesByDriverWhiteMockTest {
	static DataAccess sut;
	
	protected MockedStatic <Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	@Mock
	protected TypedQuery<Driver> queryMock;
	
	
    
	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		when(db.createQuery(anyString(), eq(Driver.class))).thenReturn(queryMock);
	    sut=new DataAccess(db);


		
    }
	@After
    public  void tearDown() {
		persistenceMock.close();


		
    }
	
	
	@Test
	// Ez dago "EzUrtzi" izeneko driverra. Null bueltatu
	public void test1() {
		try {
			when(queryMock.setParameter(eq("username"), eq("EzUrtzi"))).thenReturn(queryMock);
			when(queryMock.getSingleResult()).thenReturn(null);
			List<Ride> erantzuna = sut.getRidesByDriver("EzUrtzi");
			assertNull(erantzuna);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	// "Urtzi" db-an dago baino ez ditu ride-ak. Null bueltatu
	public void test2() {
		try {
			Driver driverUrtzi = new Driver("Urtzi", "123");
			when(queryMock.setParameter(eq("username"), eq("EzUrtzi"))).thenReturn(queryMock);
			when(queryMock.getSingleResult()).thenReturn(driverUrtzi);
			List<Ride> ridesResult = sut.getRidesByDriver("Urtzi");
			if (ridesResult.isEmpty() == true) {
				assertTrue(true);
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	// "Urtzi" db-an dago eta Ride ez aktibo bat du.
	public void test3() {
		try {
			Driver driverUrtzi = new Driver("Urtzi", "123");
			Date date = new Date();
			driverUrtzi.addRide("Donosti", "Bilbao", date, 4, 5);
			
			for (Ride ride : driverUrtzi.getCreatedRides()) {
				ride.setActive(false);
			}
			
			when(queryMock.setParameter(eq("username"), eq("EzUrtzi"))).thenReturn(queryMock);
			when(queryMock.getSingleResult()).thenReturn(driverUrtzi);
			List<Ride> ridesResult = sut.getRidesByDriver("Urtzi");
			if (ridesResult.isEmpty() == true) {
				assertTrue(true);
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	
	@Test
	// "Urtzi" db-an dago eta Ride aktibo bat du.
	public void test4() {
		try {
			Driver driverUrtzi = new Driver("Urtzi", "123");
			Date date = new Date();
			driverUrtzi.addRide("Donosti", "Bilbao", date, 4, 5);
			
			when(queryMock.setParameter(eq("username"), eq("EzUrtzi"))).thenReturn(queryMock);
			when(queryMock.getSingleResult()).thenReturn(driverUrtzi);
			List<Ride> ridesResult = sut.getRidesByDriver("Urtzi");
			if (ridesResult.isEmpty() == true) {
				fail();
			} else {
				assertTrue(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
