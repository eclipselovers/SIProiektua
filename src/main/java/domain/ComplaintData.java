package domain;

import java.util.Date;

public class ComplaintData { 
	private String sender; 
	private String receiver; 
	private Date date; 
	private Booking booking; 
	private String text; 
	private boolean aurk; 
	
	public ComplaintData(String sender, String receiver, Date date, Booking booking, String text, boolean aurk) {
		this.sender = sender;
		this.receiver = receiver;
		this.date = date;
		this.booking = booking;
		this.text = text;
		this.aurk = aurk;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
	public Date getDate() {
		return date;
	}
	public Booking getBooking() {
		return booking;
	}
	public String getText() {
		return text;
	}
	public boolean isAurk() {
		return aurk;
	}
	
	public void setAurk(boolean aurk) {
		this.aurk = aurk;
	}
	
	
}
