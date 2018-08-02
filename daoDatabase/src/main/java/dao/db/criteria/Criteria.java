package dao.db.criteria;

public abstract class Criteria<T> {
	
	public abstract String get();
	
	@Override
	public String toString() {
		return get();
	}
	
}
