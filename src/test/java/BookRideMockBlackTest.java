import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

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
import domain.Driver;
import domain.Ride;
import domain.User;

public class BookRideMockBlackTest {
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	@Mock
	TypedQuery<User> query;

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut = new DataAccess(db);
	    
	    Mockito.when(db.createQuery(
	            Mockito.eq("SELECT u FROM User u WHERE u.username = :username"),
	            Mockito.eq(User.class))).thenReturn(query);

	    Mockito.when(query.setParameter(Mockito.eq("username"), Mockito.any())).thenReturn(query);

    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	User user = new User("Traveler Test", "123456", "Traveler");
	
	

	@Test
	public void test1() {
		boolean r = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		Mockito.when(query.getSingleResult()).thenReturn(null);
		r = sut.bookRide(null, ride, 1, 1.1);
		assertFalse(r);
	}
	
	@Test
	public void test2() {
		boolean r1 = false;
		Mockito.when(query.getSingleResult()).thenReturn(user);
		r1 = sut.bookRide("Traveler Test", null, 1, 1.1);
		assertFalse(r1);
	}
	
	@Test
	public void test3() {
		boolean res = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		Mockito.when(query.getSingleResult()).thenReturn(user);
		res = sut.bookRide("Traveler Test", ride, -1, 1.1);
		assertFalse(res);
		
	}
	
	@Test
	public void test4() {
		boolean r = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		Mockito.when(query.getSingleResult()).thenReturn(user);
		r = sut.bookRide(null, ride, 4, 1.1);
		assertFalse(r);
	}
	
	@Test
	public void test5() {
		boolean r2 = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
		Mockito.when(query.getSingleResult()).thenReturn(user);
		User u = sut.getUser("Traveler Test");
		u.setMoney(110);
		sut.updateUser(u);
		r2 = sut.bookRide("Traveler Test", ride, 1, 1.1);
		assertFalse(r2);
	}
	
	@Test
	public void test6() {
		boolean r2 = false;
		Driver d = new Driver("a", "struy54");
		Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r2 = sut.bookRide("Test", ride, -1, 1.1);
			assertFalse(r2);
	}
		@Test
		public void test7() {
			boolean res = false;
			Driver d = new Driver("a", "struy54");
			Ride ride = new Ride("Donosti", "Zarautz", new Date("12/12/2025"), 2, 4.0, d);
			Mockito.when(query.getSingleResult()).thenReturn(user);
			res = sut.bookRide("Traveler Test", ride, 1, -1.1);
			assertFalse(res);
		}
		
}
	
