package dao.db.sql;

import java.sql.Connection;
import java.sql.Statement;
import dao.util.Criteria;

public abstract class Result<T> {
	
	protected Connection connection;
	protected Statement statement;
	protected String sql;
	
	protected T obj;
	protected Criteria<T> criteria;
	
	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
	
	public void setCriteria(Criteria<T> criteria) {
		this.criteria = criteria;
	}

	protected abstract String getSql();
	public abstract boolean execute();
	
	
	@Override
	public String toString() {
		return getSql();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (null==obj) {
			return false;
		}
		if (!(obj instanceof Result)) {
			return false;
		}
		return this.getSql().equals(((Result<T>) obj).getSql());
	}

	@Override
	public int hashCode() {
		return obj.hashCode();
	}

}
