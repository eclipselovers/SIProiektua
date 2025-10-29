package adapter;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

import java.util.Date;
import java.util.List;

public class DriverAdapter extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected Driver driver;
	protected String[] columnNames = new String[] {"from", "to", "date", "places", "price"};
	
	public DriverAdapter(Driver driver) {
		this.driver = driver;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		List<Ride> rides = driver.getCreatedRides();
		return rides == null ? 0 : rides.size();
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		List<Ride> rides = driver.getCreatedRides();
		if (rides == null || rowIndex < 0 || rowIndex >= rides.size()) return null;
		Ride r = rides.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return r.getFrom();
		case 1:
			return r.getTo();
		case 2:
			return r.getDate();
		case 3:
			return r.getnPlaces();
		case 4:
			return r.getPrice();
		default:
			return null;
		}
	}

}