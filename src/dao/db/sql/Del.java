package dao.db.sql;

import dao.util.DBUtil;

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
			sql.append("WHERE"+DBUtil.get(obj, DBUtil.kv(obj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

}
