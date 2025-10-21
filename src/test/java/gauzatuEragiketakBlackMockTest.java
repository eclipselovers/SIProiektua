import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
import domain.User;

public class gauzatuEragiketakBlackMockTest {
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
	
	@Mock
	TypedQuery<Double> dQuery;

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


	    Mockito.when(db.createQuery(
	            Mockito.eq("SELECT u.money FROM User u WHERE u.username = :username"),
	            Mockito.eq(Double.class))).thenReturn(dQuery);

	    Mockito.when(dQuery.setParameter(Mockito.eq("username"), Mockito.any())).thenReturn(dQuery);
		user.setMoney(10);
		Mockito.when(dQuery.getSingleResult()).thenAnswer(inv -> user.getMoney());

	    
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	User user = new User("Proba", "123456", "Driver");
	

	@Test
	public void test1() {
	
		boolean r = false;
		Mockito.when(dQuery.getSingleResult()).thenAnswer(inv -> user.getMoney());
			double adirua = sut.getActualMoney("Proba");
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r = sut.gauzatuEragiketa("Proba", 5, true);
			if(r) {
				assertEquals(adirua+5, sut.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test2() {
			boolean r1 = false;
			Mockito.when(dQuery.getSingleResult()).thenAnswer(inv -> user.getMoney());
			double adirua = sut.getActualMoney("Proba");
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r1 = sut.gauzatuEragiketa("Proba", 5, false);
			if(r1) {
				assertEquals(adirua-5,sut.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
	}
	
	@Test
	public void test3() {
			boolean r2 = false;
			Mockito.when(dQuery.getSingleResult()).thenAnswer(inv -> user.getMoney());
			double adirua = sut.getActualMoney("Proba");
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r2 = sut.gauzatuEragiketa("Proba", adirua+5, false);
			if(r2) {
				assertEquals(0,sut.getActualMoney("Proba"), 0.0001);
			} else {
				fail();
			}
			
			
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			Mockito.when(query.getSingleResult()).thenReturn(null);
			r = sut.gauzatuEragiketa(null, 5, false);
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		try {
			boolean r = false;
			Mockito.when(query.getSingleResult()).thenThrow(new NoResultException());
			r = sut.gauzatuEragiketa("admin", 5, true);
			assertFalse(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	
}