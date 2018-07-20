package dao.db.sql;

import dao.db.util.DBExecute;
import dao.db.util.DBUtil;

public class Del<T> extends Update<T>{
	
	public Del() {
		super();
	}

	public Del(T obj) {
		super(obj);
	}

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		StringBuffer sql = new StringBuffer("DELETE FROM "+DBUtil.getTableName(obj));
		if (null==criteria) {
			sql.append(" WHERE "+criteria.toString());
		}
		else {
			sql.append("WHERE"+DBUtil.get(clazz, DBUtil.kv(obj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.DELETE;
	}

}
