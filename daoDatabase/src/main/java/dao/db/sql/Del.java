package dao.db.sql;

import dao.db.util.DBExecute;

public class Del<T> extends Update<T>{
	
	public Del() {
		super();
	}

	public Del(T obj) {
		super(obj);
	}

	@SuppressWarnings("RedundantExplicitVariableType")
    @Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
        StringBuilder sql = new StringBuilder("DELETE FROM " + DBUtil.getTableName(obj));
		if (null==criteria) {
			sql.append(" WHERE ").append(criteria.toString());
		}
		else {
			sql.append("WHERE").append(DBUtil.get(clazz, DBUtil.kv(obj), "AND"));
		}
		this.sql = sql.toString();
		return this.sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.DELETE;
	}

}
