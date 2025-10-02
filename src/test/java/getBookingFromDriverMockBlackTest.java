import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;

public class getBookingFromDriverMockBlackTest {

    static DataAccess sut;  // system under test

    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    private Driver driver1;
    private Driver driver2;
    private Driver driver3;
    private Ride rideInactive;
    private Ride rideActive;
    
    @Mock
	TypedQuery<Driver> dquery;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);

        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(entityManagerFactory);
        when(entityManagerFactory.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);
        
        Mockito.when(db.createQuery(
	            Mockito.eq("SELECT d FROM Driver d WHERE d.username = :username"),
	            Mockito.eq(Driver.class))).thenReturn(dquery);

	    Mockito.when(dquery.setParameter(Mockito.eq("username"), Mockito.any())).thenReturn(dquery);


        sut = new DataAccess(db);

        driver1 = new Driver("Driver", "123");
        driver2 = new Driver("Driver2", "123");
        driver3 = new Driver("Driver3", "123");

        rideInactive = new Ride("a", "a", new Date(2025, 10, 20), 231, 2, driver2);
        rideInactive.setActive(false);

        rideActive = new Ride("aas", "wefef", new Date(2025, 2, 20), 5, 5, driver3);
        rideActive.setBookings(new ArrayList<>());  
        Traveler traveler = new Traveler("a", "a");
        Booking booking = new Booking(rideActive, traveler, 2); 
        rideActive.getBookings().add(booking);

        driver3.getCreatedRides().add(rideActive);

//        when(db.find(Driver.class, "Driver")).thenReturn(driver1);
//        when(db.find(Driver.class, "Driver2")).thenReturn(driver2);
//        when(db.find(Driver.class, "Driver3")).thenReturn(driver3);
//        when(db.find(Driver.class, null)).thenReturn(null);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
    }

    @Test
    public void test1() {
    	Mockito.when(dquery.getSingleResult()).thenReturn(null);
        assertEquals(null, sut.getBookingFromDriver(null));
    }

    @Test
    public void test2() {
    	Mockito.when(dquery.getSingleResult()).thenReturn(driver1);
        List<Booking> expected = new ArrayList<>();
        assertEquals(expected, sut.getBookingFromDriver("Driver"));
    }

    @Test
    public void test3() {
    	List<Booking> expected = new ArrayList<>();
    	Mockito.when(dquery.getSingleResult()).thenReturn(driver2);
        assertEquals(expected, sut.getBookingFromDriver("Driver2"));
    }

    @Test
    public void test4() {
    	Mockito.when(dquery.getSingleResult()).thenReturn(driver3);
        List<Booking> expected = new ArrayList<>(rideActive.getBookings());
        assertEquals(expected, sut.getBookingFromDriver("Driver3"));
    }
    @Test
    public void test5() {
    	Mockito.when(dquery.getSingleResult()).thenReturn(null);
        assertEquals(null, sut.getBookingFromDriver("notinthedatabase"));
    }
    
     
    //EZIN DA FROGATU STRING EZ DENEAN
}
