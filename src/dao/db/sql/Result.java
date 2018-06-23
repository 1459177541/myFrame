package dao.db.sql;

import java.sql.Connection;
import java.sql.Statement;
import dao.util.Criteria;
import dao.util.DBExecute;

public abstract class Result<T> {
	
	protected Connection connection;
	protected Statement statement;
	protected String sql;
	
	protected T obj;
	protected Criteria<T> criteria;
	
	public T getObj() {
		return obj;
	}

	public Result<T> setObj(T obj) {
		this.obj = obj;
		return this;
	}
	
	public Result<T> setCriteria(Criteria<T> criteria) {
		this.criteria = criteria;
		return this;
	}

	protected abstract String getSql();
	public abstract boolean execute();
	public abstract DBExecute getState();
	
	
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
