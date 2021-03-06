package dao.db.sql;

import dao.db.util.DBExecute;

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
        StringBuilder sql = new StringBuilder("UPDATE " + DBUtil.getTableName(obj) + " SET " + DBUtil.get(clazz, DBUtil.kv(obj), ","));
		if (null==criteria) {
			sql.append(" WHERE ").append(criteria.toString());
		}
		else {
            sql.append("WHERE").append(DBUtil.get(clazz, DBUtil.kv(oldObj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.EDIT;
	}

}
