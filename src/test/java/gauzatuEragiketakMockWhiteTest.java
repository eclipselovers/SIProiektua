import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import domain.User;

public class gauzatuEragiketakMockWhiteTest {
	
	
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
	
	
	User user = new User("Proba", "123456", "Driver");
	
	

	@Test
	public void test1() {
		boolean r = false;
		Mockito.when(query.getSingleResult()).thenReturn(null);
		r = sut.gauzatuEragiketa(null, 0, true);
		assertFalse(r);
	}
	
	@Test
	public void test2() {
		try {
			boolean r1 = false;
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r1 = sut.gauzatuEragiketa("Proba", 0, false);
			assertTrue(r1);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test3() {
		try {
			boolean r2 = false;
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r2 = sut.gauzatuEragiketa("Proba", 0, true);
			assertTrue(r2);
		}catch(Exception e1) {
			fail();
		}
	}
	
	@Test
	public void test4() {
		try {
			boolean r = false;
			Mockito.when(query.getSingleResult()).thenReturn(user);
			r = sut.gauzatuEragiketa("Proba", 1000, false);
			assertTrue(r);
		}catch(Exception e1) {
			fail();
		}
	}
	
	
}