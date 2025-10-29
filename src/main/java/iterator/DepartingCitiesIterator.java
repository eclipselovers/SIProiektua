package iterator;

public class DepartingCitiesIterator implements ExtendedIterator<String> {

	private java.util.List<String> cities;
	private int position = -1;

	public DepartingCitiesIterator(java.util.List<String> cities) {
		this.cities = cities;
	}

	@Override
	public boolean hasNext() {
		return position < cities.size() - 1;
	}

	@Override
	public String next() {
		position++;
		return cities.get(position);
	}

	@Override
	public String previous() {
		position--;
		return cities.get(position);
	}

	@Override
	public boolean hasPrevious() {
		return position > 0;
	}

	@Override
	public void goFirst() {
		position = 0;
	}

	@Override
	public void goLast() {
		position = cities.size() - 1;
	}

}
