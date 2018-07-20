package dao.db.sql;

import dao.db.util.DBExecute;
import dao.db.util.DBUtil;

public class Edit<T> extends Update<T> {

	private T oldObj;
	
	public Edit() {
		super();
	}

	public Edit(T obj,T oldObj) {
		super(obj);
		this.oldObj = oldObj;
	}

	public void setOldObj(T oldObj) {
		this.oldObj = oldObj;
	}

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		StringBuffer sql = new StringBuffer("UPDATE "+DBUtil.getTableName(obj)+" SET "+DBUtil.get(clazz, DBUtil.kv(obj), ","));
		if (null==criteria) {
			sql.append(" WHERE "+criteria.toString());
		}
		else {
			sql.append("WHERE"+DBUtil.get(clazz, DBUtil.kv(oldObj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.EDIT;
	}

}
