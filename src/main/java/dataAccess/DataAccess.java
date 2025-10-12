package dataAccess;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import businessLogic.RideRequest;
import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;
	private static String donostia = "Donostia";

	ConfigXML c = ConfigXML.getInstance();
	
	private String adminPass="admin";

	public DataAccess() {
		if (c.isDatabaseInitialized()) {
		    String fileName = c.getDbFilename();
		    Path filePath = Paths.get(fileName);

		    try {
		        Files.delete(filePath);
		        Files.deleteIfExists(Paths.get(fileName + "$"));
		        System.out.println("File deleted");
		    } catch (IOException e) {
		        System.out.println("Operation failed");
		    }
		}

		open();
		if (c.isDatabaseInitialized()) {
			initializeDB();
		}

		System.out.println("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}
	//This constructor is used to mock the DB
	public DataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		db.getTransaction().begin();
		try {
			Driver driver1 = new Driver("Urtzi", "123");
			driver1.setMoney(15);
			driver1.setBalorazioa(14);
			driver1.setBalkop(3);
			Driver driver2 = new Driver("Zuri", "456");
			driver2.setBalorazioa(10);
			driver2.setBalkop(3);
			db.persist(driver1);
			db.persist(driver2);

			Traveler traveler1 = new Traveler("Unax", "789");
			traveler1.setIzoztatutakoDirua(68);
			traveler1.setMoney(100);
			traveler1.setBalorazioa(14);
			traveler1.setBalkop(4);
			Traveler traveler2 = new Traveler("Luken", "abc");
			traveler2.setBalorazioa(4);
			traveler2.setBalkop(3);
			db.persist(traveler1);
			db.persist(traveler2);

			Calendar cal = Calendar.getInstance();
			cal.set(2024, Calendar.MAY, 20);
			Date date1 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.MAY, 30);
			Date date2 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.MAY, 10);
			Date date3 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.APRIL, 20);
			Date date4 = UtilDate.trim(cal.getTime());

			driver1.addRide(donostia, "Madrid", date2, 5, 20); //ride1
			driver1.addRide("Irun", donostia, date2, 5, 2); //ride2
			driver1.addRide("Madrid", donostia, date3, 5, 5); //ride3
			driver1.addRide("Barcelona", "Madrid", date4, 0, 10); //ride4
			driver2.addRide(donostia, "Hondarribi", date1, 5, 3); //ride5

			Ride ride1 = driver1.getCreatedRides().get(0);
			Ride ride2 = driver1.getCreatedRides().get(1);
			Ride ride3 = driver1.getCreatedRides().get(2);
			Ride ride4 = driver1.getCreatedRides().get(3);
			Ride ride5 = driver2.getCreatedRides().get(0);

			Booking book1 = new Booking(ride4, traveler1, 2);
			Booking book2 = new Booking(ride1, traveler1, 2);
			Booking book4 = new Booking(ride3, traveler1, 1);
			Booking book3 = new Booking(ride2, traveler2, 2);
			Booking book5 = new Booking(ride5, traveler1, 1);

			book1.setStatus("Accepted");
			book2.setStatus("Rejected");
			book3.setStatus("Accepted");
			book4.setStatus("Accepted");
			book5.setStatus("Accepted");

			db.persist(book1);
			db.persist(book2);
			db.persist(book3);
			db.persist(book4);
			db.persist(book5);

			Movement m1 = new Movement(traveler1, "BookFreeze", 20);
			Movement m2 = new Movement(traveler1, "BookFreeze", 40);
			Movement m3 = new Movement(traveler1, "BookFreeze", 5);
			Movement m4 = new Movement(traveler2, "BookFreeze", 4);
			Movement m5 = new Movement(traveler1, "BookFreeze", 3);
			Movement m6 = new Movement(driver1, "Deposit", 15);
			Movement m7 = new Movement(traveler1, "Deposit", 168);
			
			db.persist(m6);
			db.persist(m7);
			db.persist(m1);
			db.persist(m2);
			db.persist(m3);
			db.persist(m4);
			db.persist(m5);
			
			traveler1.addBookedRide(book1);
			traveler1.addBookedRide(book2);
			traveler2.addBookedRide(book3);
			traveler1.addBookedRide(book4);
			traveler1.addBookedRide(book5);
			db.merge(traveler1);

			Car c1 = new Car("1234ABC", "Renault", 5);
			Car c2 = new Car("5678DEF", "Citroen", 3);
			Car c3 = new Car("9101GHI", "Audi", 5);
			driver1.addCar(c1);
			driver1.addCar(c2);
			driver2.addCar(c3);
			db.persist(c1);
			db.persist(c2);
			db.persist(c3);

			//Admin a1 = new Admin("Jon", "111");
			//db.persist(a1);

			Discount dis = new Discount("Uda24", 0.2, true);
			db.persist(dis);

			db.getTransaction().commit();
			System.out.println("Db initialized");

		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;

	}

	
	public Ride createRide(RideRequest request)  
	        throws RideAlreadyExistException, RideMustBeLaterThanTodayException { 
	  
	    validateRideCreationInputs(request.getDate(), request.getDriverName()); 
	    Driver driver = findDriver(request.getDriverName()); 
	  
	    db.getTransaction().begin(); 
	    checkRideDoesNotExist(driver, request.getFrom(), request.getTo(), request.getDate()); 
	    Ride ride = persistNewRide(driver, request.getFrom(), request.getTo(), request.getDate(), 
	                               request.getNPlaces(), request.getPrice()); 
	    db.getTransaction().commit(); 
	  
	    return ride; 
	} 

	private void validateRideCreationInputs(Date date, String driverName) throws RideMustBeLaterThanTodayException { 
		 if (driverName == null) { 
		 	throw new IllegalArgumentException("Driver name cannot be null"); 
		 } 
		 if (new Date().compareTo(date) > 0) { 
		 	throw new RideMustBeLaterThanTodayException(	ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday")); 
		 } 
		} 
		 
		private Driver findDriver(String driverName) { 
		return db.find(Driver.class, driverName); 
		} 
		 
		private void checkRideDoesNotExist(Driver driver, String from, String to, Date date) 
		throws RideAlreadyExistException { 
		if (driver.doesRideExists(from, to, date)) { 
		db.getTransaction().commit(); 
		 	throw new RideAlreadyExistException(		ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist")); 
		 } 
		} 
		 
		private Ride persistNewRide(Driver driver, String from, String to, Date date, int nPlaces, float price) { 
		 Ride ride = driver.addRide(from, to, date, nPlaces, price); 
		 db.persist(driver); 
		 return ride; 
		} 
	
	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getActiveRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery(
				"SELECT r FROM Ride r WHERE r.from = ?1 AND r.to = ?2 AND r.date = ?3 AND r.active = true", Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getThisMonthActiveRideDates");

		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4 AND r.active = true",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		res.addAll(dates);

		return res;
	}

	public void open() {

		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

	}

	public void close() {
		db.close();
		System.out.println("DataAcess closed");
	}

	public User getUser(String erab) {
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		query.setParameter("username", erab);
		return query.getSingleResult();
	}

	public double getActualMoney(String erab) {
		TypedQuery<Double> query = db.createQuery("SELECT u.money FROM User u WHERE u.username = :username",
				Double.class);
		query.setParameter("username", erab);
		Double money = query.getSingleResult();
		if (money != null) {
			return money;
		} else {
			return 0;
		}
	}

	public boolean isRegistered(String erab, String passwd) {
		TypedQuery<Long> travelerQuery = db.createQuery(
				"SELECT COUNT(t) FROM Traveler t WHERE t.username = :username AND t.passwd = :passwd", Long.class);
		travelerQuery.setParameter("username", erab);
		travelerQuery.setParameter("passwd", passwd);
		Long travelerCount = travelerQuery.getSingleResult();

		TypedQuery<Long> driverQuery = db.createQuery(
				"SELECT COUNT(d) FROM Driver d WHERE d.username = :username AND d.passwd = :passwd", Long.class);
		driverQuery.setParameter("username", erab);
		driverQuery.setParameter("passwd", passwd);
		Long driverCount = driverQuery.getSingleResult();

		/*TypedQuery<Long> adminQuery = db.createQuery(
				"SELECT COUNT(a) FROM Admin a WHERE a.username = :username AND a.passwd = :passwd", Long.class);
		adminQuery.setParameter("username", erab);
		adminQuery.setParameter("passwd", passwd);
		Long adminCount = adminQuery.getSingleResult();*/

		boolean isAdmin=((erab.compareTo("admin")==0) && (passwd.compareTo(adminPass)==0));
		return travelerCount > 0 || driverCount > 0 || isAdmin;
	}

//	public Driver getDriver(String erab) {
//		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
//		query.setParameter("username", erab);
//		List<Driver> resultList = query.getResultList();
//		if (resultList.isEmpty()) {
//			return null;
//		} else {
//			return resultList.get(0);
//		}
//	}
//
//	public Traveler getTraveler(String erab) {
//		TypedQuery<Traveler> query = db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username",
//				Traveler.class);
//		query.setParameter("username", erab);
//		List<Traveler> resultList = query.getResultList();
//		if (resultList.isEmpty()) {
//			return null;
//		} else {
//			return resultList.get(0);
//		}
//	}

	private <T extends User> T getUserByUsername(Class<T> clazz, String username) { 
	    TypedQuery<T> query = db.createQuery( 
	        "SELECT u FROM " + clazz.getSimpleName() + " u WHERE u.username = :username", clazz); 
	    query.setParameter("username", username); 
	    List<T> resultList = query.getResultList(); 
	    return resultList.isEmpty() ? null : resultList.get(0); 
	} 
	public Driver getDriver(String erab) { 
	    return getUserByUsername(Driver.class, erab); 
	} 
	  
	public Traveler getTraveler(String erab) { 
	    return getUserByUsername(Traveler.class, erab); 
	}
	
	/*public Admin getAdmin(String erab) {
		TypedQuery<Admin> query = db.createQuery("SELECT a FROM Admin a WHERE t.username = :username", Admin.class);
		query.setParameter("username", erab);
		List<Admin> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}*/

	public String getMotabyUsername(String erab) {
		TypedQuery<String> driverQuery = db.createQuery("SELECT d.mota FROM Driver d WHERE d.username = :username",
				String.class);
		driverQuery.setParameter("username", erab);
		List<String> driverResultList = driverQuery.getResultList();

		TypedQuery<String> travelerQuery = db.createQuery("SELECT t.mota FROM Traveler t WHERE t.username = :username",
				String.class);
		travelerQuery.setParameter("username", erab);
		List<String> travelerResultList = travelerQuery.getResultList();

		/*TypedQuery<String> adminQuery = db.createQuery("SELECT a.mota FROM Admin a WHERE a.username = :username",
				String.class);
		adminQuery.setParameter("username", erab);
		List<String> adminResultList = adminQuery.getResultList();*/

		if (!driverResultList.isEmpty()) {
			return driverResultList.get(0);
		} else if (!travelerResultList.isEmpty()) {
			return travelerResultList.get(0);
		} else  {
			return "Admin";
		} 
	}

//	public boolean addDriver(String username, String password) { 
//	    try { 
//	        db.getTransaction().begin(); 
//	  
//	        if (!isUsernameAvailable(username)) return false; 
//	  
//	        persistNewDriver(username, password); 
//	  
//	        db.getTransaction().commit(); 
//	        return true; 
//	    } catch (Exception e) { 
//	        e.printStackTrace(); 
//	        db.getTransaction().rollback(); 
//	        return false; 
//	    } 
//	} 
//	  
	
//	  
//	private void persistNewDriver(String username, String password) { 
//	    Driver driver = new Driver(username, password); 
//	    db.persist(driver); 
//	} 
	 
	

//	public boolean addTraveler(String username, String password) {
//		try {
//			db.getTransaction().begin();
//
//			Driver existingDriver = getDriver(username);
//			Traveler existingTraveler = getTraveler(username);
//			if (existingDriver != null || existingTraveler != null) {
//				return false;
//			}
//
//			Traveler traveler = new Traveler(username, password);
//			db.persist(traveler);
//			db.getTransaction().commit();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			db.getTransaction().rollback();
//			return false;
//		}
//	}
	
	public <T> boolean addUser(String username, String password, Class<T> mota) { 
		try { 
			db.getTransaction().begin(); 
			if (!isUsernameAvailable(username)) return false; 
			persistNewUser(username, password, mota); 
			db.getTransaction().commit(); 
			return true; 
		}catch(Exception e) { 
			db.getTransaction().rollback(); 
			return false; 
		} 
	} 
	
	private <T> void persistNewUser(String username, String password, Class<T> mota) throws Exception { 
		Constructor<T> constr = mota.getConstructor(String.class, String.class);
		T user = constr.newInstance(username, password);
		db.persist(user);
	} 
	
	private boolean isUsernameAvailable(String username) { 
	    return getDriver(username) == null && getTraveler(username) == null; 
	} 

	public boolean gauzatuEragiketa(String username, double amount, boolean deposit) { 
		try { 
			db.getTransaction().begin(); 
			User user = getUser(username); 
			if (user != null) { 
				return diruMugimenduak(user, amount, deposit); 
			} 
			db.getTransaction().commit(); 
			return false; 
		} catch (Exception e) { 
			e.printStackTrace(); 
			db.getTransaction().rollback(); 
			return false; 
		} 
	} 
	
	public boolean diruMugimenduak(User user, double amount, boolean deposit) {
		double currentMoney = user.getMoney();
		if (deposit) {
			user.setMoney(currentMoney + amount);
		} else {
			if ((currentMoney - amount) < 0)
				user.setMoney(0);
			else
				user.setMoney(currentMoney - amount);
		}
		db.merge(user);
		db.getTransaction().commit();
		return true;
		
	}

	public void addMovement(User user, String eragiketa, double amount) {
		try {
			db.getTransaction().begin();

			Movement movement = new Movement(user, eragiketa, amount);
			db.persist(movement);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

//	public boolean bookRide(String username, Ride ride, int seats, double desk) {
//		try {
//			db.getTransaction().begin();
//
//			Traveler traveler = getTraveler(username);
//			if (traveler == null) {
//				return false;
//			}
//
//			if (ride.getnPlaces() < seats) {
//				return false;
//			}
//
//			double ridePriceDesk = (ride.getPrice() - desk) * seats;
//			double availableBalance = traveler.getMoney();
//			if (availableBalance < ridePriceDesk) {
//				return false;
//			}
//
//			Booking booking = new Booking(ride, traveler, seats);
//			booking.setTraveler(traveler);
//			booking.setDeskontua(desk);
//			db.persist(booking);
//
//			ride.setnPlaces(ride.getnPlaces() - seats);
//			traveler.addBookedRide(booking);
//			traveler.setMoney(availableBalance - ridePriceDesk);
//			traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() + ridePriceDesk);
//			db.merge(ride);
//			db.merge(traveler);
//			db.getTransaction().commit();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			db.getTransaction().rollback();
//			return false;
//		}
//	}
	
	public boolean bookRide(String username, Ride ride, int seats, double desk) { 
	    try { 
	        db.getTransaction().begin(); 
	  
	        Traveler traveler = getTraveler(username); 
	        if (!isBookingValid(traveler, ride, seats, desk)) return false; 
	  
	        Booking booking = createBooking(traveler, ride, seats, desk); 
	        updateBalancesAndSeats(traveler, ride, booking, desk); 
	  
	        db.getTransaction().commit(); 
	        return true; 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	        db.getTransaction().rollback(); 
	        return false; 
	    } 
	} 
	  
	private boolean isBookingValid(Traveler traveler, Ride ride, int seats, double desk) { 
	    if (traveler == null) return false; 
	    if (ride.getnPlaces() < seats) return false; 
	    double totalPrice = (ride.getPrice() - desk) * seats; 
	    return traveler.getMoney() >= totalPrice; 
	} 
	  
	private Booking createBooking(Traveler traveler, Ride ride, int seats, double desk) { 
	    Booking booking = new Booking(ride, traveler, seats); 
	    booking.setTraveler(traveler); 
	    booking.setDeskontua(desk); 
	    db.persist(booking); 
	    return booking; 
	} 
	  
	private void updateBalancesAndSeats(Traveler traveler, Ride ride, Booking booking, double desk) { 
	    double totalPrice = (ride.getPrice() - desk) * booking.getSeats(); 
	    ride.setnPlaces(ride.getnPlaces() - booking.getSeats()); 
	    traveler.addBookedRide(booking); 
	    traveler.setMoney(traveler.getMoney() - totalPrice); 
	    traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() + totalPrice); 
	    db.merge(ride); 
	    db.merge(traveler); 
	} 
	 

	public List<Movement> getAllMovements(User user) {
		TypedQuery<Movement> query = db.createQuery("SELECT m FROM Movement m WHERE m.user = :user", Movement.class);
		query.setParameter("user", user);
		return query.getResultList();
	}

	public List<Booking> getBookedRides(String username) {
		db.getTransaction().begin();
		Traveler trav = getTraveler(username);
		db.getTransaction().commit();
		return trav.getBookedRides();
	}

	public <T> boolean updateEntity(T entity) { 
		try { 
			db.getTransaction().begin(); 
			db.merge(entity); 
			db.getTransaction().commit(); 
			return true; 
		} catch (Exception e) { 
			e.printStackTrace(); 
			db.getTransaction().rollback(); 
			return false; 
		} 
	} 

	public void updateTraveler(Traveler traveler) { 
		updateEntity(traveler); 
	} 
	
	public void updateDriver(Driver driver) { 
		updateEntity(driver); 
	} 
 
	public void updateUser(User user) { 
		updateEntity(user); 
	} 

	public List<Booking> getPastBookedRides(String username) {
		TypedQuery<Booking> query = db.createQuery(
				"SELECT b FROM Booking b WHERE b.traveler.username = :username AND b.ride.date <= CURRENT_DATE",
				Booking.class);
		query.setParameter("username", username);
		return query.getResultList();
	}

	public void updateBooking(Booking booking) {
		try {
			db.getTransaction().begin();
			db.merge(booking);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<Booking> getBookingFromDriver(String username) {
		try {
			db.getTransaction().begin();
			TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username",
					Driver.class);
			query.setParameter("username", username);
			Driver driver = query.getSingleResult();

			List<Ride> rides = driver.getCreatedRides();
			List<Booking> bookings = new ArrayList<>();

			for (Ride ride : rides) {
				if (ride.isActive()) {
					bookings.addAll(ride.getBookings());
				}
			}

			db.getTransaction().commit();
			return bookings;
		} catch (Exception e) {
		//	e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void cancelRide(Ride ride) {
	    try {
	        db.getTransaction().begin();
	        for (Booking booking : ride.getBookings()) {
	            if ("Accepted".equals(booking.getStatus()) || "NotDefined".equals(booking.getStatus()))
	                kudeatuOnartutakoErreserba(booking);
	            booking.setStatus("Rejected");
	            db.merge(booking);
	        }
	        ride.setActive(false);
	        db.merge(ride);
	        db.getTransaction().commit();
	    } catch (Exception e) {
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        e.printStackTrace();
	    }
	}

	private void kudeatuOnartutakoErreserba(Booking booking) {
	    double price = booking.prezioaKalkulatu();
	    Traveler traveler = booking.getTraveler();
	    traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() - price);
	    traveler.setMoney(traveler.getMoney() + price);
	    db.merge(traveler);
	    db.getTransaction().commit();
	    addMovement(traveler, "BookDeny", price);
	    db.getTransaction().begin();
	}

	public List<Ride> getRidesByDriver(String username) {
		try {
			db.getTransaction().begin();
			TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
			query.setParameter("username", username);
			Driver driver = query.getSingleResult();
			List<Ride> rides = driver.getCreatedRides();
			List<Ride> activeRides = new ArrayList<>();
			for (Ride ride : rides) {
				if (ride.isActive()) {
					activeRides.add(ride);
				}
			}
			db.getTransaction().commit();
			return activeRides;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public boolean addCar(String username, Car kotxe) {
		try {
			boolean b = isAdded(username, kotxe.getMatrikula());
			if (!b) {
				db.getTransaction().begin();
				Driver dri = getDriver(username);
				dri.addCar(kotxe);
				db.persist(dri);
				db.getTransaction().commit();
			}
			return !b;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public boolean isAdded(String username, String matr) {
		boolean era = false;
		for (Car kotxe : getDriver(username).getCars()) {
			if (kotxe.getMatrikula().equals(matr)) {
				era = true;
			}
		}
		return era;
	}

	public boolean erreklamazioaBidali(ComplaintData data) { 
		try { 
			db.getTransaction().begin(); 
			Complaint erreklamazioa = new Complaint(data.getSender(), data.getReceiver(), data.getDate(), data.getBooking(), data.getText(), data.isAurk()); 
			db.persist(erreklamazioa); 
			db.getTransaction().commit(); 
			return true; 
		} catch (Exception e) { 
			e.printStackTrace(); 
			db.getTransaction().rollback(); 
			return false; 
		} 
	} 

	public void updateComplaint(Complaint erreklamazioa) {
		try {
			db.getTransaction().begin();
			db.merge(erreklamazioa);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public Car getKotxeByMatrikula(String matrikula) {
		TypedQuery<Car> query = db.createQuery("SELECT k FROM Car k WHERE k.matrikula = :matrikula", Car.class);
		query.setParameter("matrikula", matrikula);
		List<Car> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	private boolean persistEntity(Object entity) {
	    try {
	        db.getTransaction().begin();
	        db.persist(entity);
	        db.getTransaction().commit();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        return false;
	    }
	}

	public boolean createDiscount(Discount di) {
	    return persistEntity(di);
	}

	public boolean createAlert(Alert alert) {
	    return persistEntity(alert);
	}

	public List<Discount> getAllDiscounts() {
		try {
			db.getTransaction().begin();
			TypedQuery<Discount> query = db.createQuery("SELECT d FROM Discount d ", Discount.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void deleteDiscount(Discount dis) {
		try {
			db.getTransaction().begin();
			db.remove(dis);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public void updateDiscount(Discount dis) {
		try {
			db.getTransaction().begin();
			db.merge(dis);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public Discount getDiscount(String kodea) {
		TypedQuery<Discount> query = db.createQuery("SELECT d FROM Discount d WHERE d.kodea = :kodea", Discount.class);
		query.setParameter("kodea", kodea);
		List<Discount> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public void deleteCar(Car car) {
		try {
			db.getTransaction().begin();

			Car managedCar = db.merge(car);
			db.remove(managedCar);
			Driver driver = managedCar.getDriver();
			driver.removeCar(managedCar);
			db.merge(driver);

			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<User> getUserList() {
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}

	public void deleteUser(User us) { 
		try { 
			if (us.getMota().equals("Driver")) { 
				deleteDriverUser(us); 
			} else { 
				deleteTravelerUser(us); 
			} 
			removeUserFromDatabase(us); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 

	private void deleteDriverUser(User user) { 
		String username = user.getUsername(); 
		List<Ride> rides = getRidesByDriver(username); 
		if (rides != null) { 
			for (Ride ride : rides) { 
					cancelRide(ride); 
			} 
		} 
		Driver driver = getDriver(username); 
		List<Car> cars = driver.getCars(); 
			if (cars != null) { 
				for (int i = cars.size() - 1; i >= 0; i--) { 
					deleteCar(cars.get(i)); 
				} 
			} 
	} 

		 
	private void deleteTravelerUser(User user) { 
		String username = user.getUsername(); 
		List<Booking> bookings = getBookedRides(username); 
		if (bookings != null) { 
			for (Booking booking : bookings) { 
				rejectBooking(booking); 
			} 
		} 
		List<Alert> alerts = getAlertsByUsername(username); 
		if (alerts != null) { 
			for (Alert alert : alerts) { 
				deleteAlert(alert.getAlertNumber()); 
			} 
		} 
	} 

	private void rejectBooking(Booking booking) { 
		booking.setStatus("Rejected"); 
		Ride ride = booking.getRide(); 
		ride.setnPlaces(ride.getnPlaces() + booking.getSeats()); 
	} 

	private void removeUserFromDatabase(User user) { 
		db.getTransaction().begin(); 
		user = db.merge(user); 
		db.remove(user); 
		db.getTransaction().commit(); 
} 
	
	public List<Alert> getAlertsByUsername(String username) {
		try {
			db.getTransaction().begin();

			TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.traveler.username = :username",
					Alert.class);
			query.setParameter("username", username);
			List<Alert> alerts = query.getResultList();

			db.getTransaction().commit();

			return alerts;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public Alert getAlert(int alertNumber) {
		try {
			db.getTransaction().begin();
			TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.alertNumber = :alertNumber",
					Alert.class);
			query.setParameter("alertNumber", alertNumber);
			Alert alert = query.getSingleResult();
			db.getTransaction().commit();
			return alert;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void updateAlert(Alert alert) {
		try {
			db.getTransaction().begin();
			db.merge(alert);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}

	}

	public boolean updateAlertaAurkituak(String username) { 
		try { 
			db.getTransaction().begin(); 
			boolean alertFound = false; 
			TypedQuery<Alert> alertQuery = db.createQuery("SELECT a FROM Alert a WHERE a.traveler.username = :username", Alert.class); 
			alertQuery.setParameter("username", username); 
			List<Alert> alerts = alertQuery.getResultList();  
			TypedQuery<Ride> rideQuery = db.createQuery("SELECT r FROM Ride r WHERE r.date > CURRENT_DATE AND r.active = true", Ride.class); 
			List<Ride> rides = rideQuery.getResultList(); 		 
				for (Alert alert : alerts) { 
					boolean found = findAlerts(rides, alert); 
					alert.setFound(found);	 
					if (alert.isActive() && found) 
						alertFound = true; 		
						db.merge(alert);
					} 	 
				db.getTransaction().commit(); 
				return alertFound; 
		} catch (Exception e) { 
			e.printStackTrace(); 
			db.getTransaction().rollback(); 
			return false; 
		} 
	} 

		 
	public boolean findAlerts(List<Ride> rides, Alert alert) {
		for (Ride ride : rides) {
			if (UtilDate.datesAreEqualIgnoringTime(ride.getDate(), alert.getDate())
					&& ride.getFrom().equals(alert.getFrom()) && ride.getTo().equals(alert.getTo())
					&& ride.getnPlaces() > 0) {
				return true;
			}
		}	
		return false;
	}

	public boolean deleteAlert(int alertNumber) {
		try {
			db.getTransaction().begin();

			TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.alertNumber = :alertNumber",
					Alert.class);
			query.setParameter("alertNumber", alertNumber);
			Alert alert = query.getSingleResult();

			Traveler traveler = alert.getTraveler();
			traveler.removeAlert(alert);
			db.merge(traveler);

			db.remove(alert);

			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public Complaint getComplaintsByBook(Booking book) {
		TypedQuery<Complaint> query = db.createQuery("SELECT DISTINCT c FROM Complaint c WHERE c.booking = :book",
				Complaint.class);
		query.setParameter("book", book);

		List<Complaint> erreklamazioa = query.getResultList();
		if (!erreklamazioa.isEmpty()) {
			return erreklamazioa.get(0);
		} else {
			return null;
		}
	}

}
