package dao.db.sql;

import dao.util.DBExecute;
import dao.util.DBUtil;

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
		StringBuffer sql = new StringBuffer("UPDATE "+DBUtil.getTableName(obj)+" SET "+DBUtil.get(obj, DBUtil.kv(obj), ","));
		if (null==criteria) {
			sql.append(" WHERE "+criteria.toString());
		}
		else {
			sql.append("WHERE"+DBUtil.get(oldObj, DBUtil.kv(oldObj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.EDIT;
	}

}
