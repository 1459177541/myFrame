package dao.db.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import dao.db.util.Criteria;
import dao.db.util.DBExecute;

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
	public abstract DBExecute getState();
	public abstract boolean execute();

	/**\
	 * 执行
	 * @return 执行是否成功
	 */
	public boolean check(){
		if (null==connection||null==obj) {
			return false;
		}
		sql = Objects.requireNonNullElseGet(sql,()->getSql());
		if (null==sql) {
			return false;
		}
		statement = Objects.requireNonNullElseGet(statement,()-> {
			try {
				return connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		});
		if(null==statement){
			return false;
		}
		return true;
	}

	
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
