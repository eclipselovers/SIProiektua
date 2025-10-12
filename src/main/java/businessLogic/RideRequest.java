package businessLogic;

import java.util.Date;

public class RideRequest { 
    private String from; 
    private String to; 
    private Date date; 
    private int nPlaces; 
    private float price; 
    private String driverName; 
  
    public RideRequest(String from, String to, Date date, int nPlaces, float price, String driverName) { 
        this.from = from; 
        this.to = to; 
        this.date = date; 
        this.nPlaces = nPlaces; 
        this.price = price; 
        this.driverName = driverName; 
    } 
  
    public String getFrom() { return from; } 
    public String getTo() { return to; } 
    public Date getDate() { return date; } 
    public int getNPlaces() { return nPlaces; } 
    public float getPrice() { return price; } 
    public String getDriverName() { return driverName; } 
} 