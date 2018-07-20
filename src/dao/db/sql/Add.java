package dao.db.sql;

import dao.db.util.DBExecute;
import dao.db.util.DBUtil;

public class Add<T> extends Update<T> {

	public Add() {
		super();
	}

	public Add(T obj) {
		super(obj);
	}

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		if (null==sql) {
			sql="INSERT INTO "+DBUtil.getTableName(obj)+DBUtil.get(clazz, DBUtil.key(), ",")+" "
					+ "VALUES"
					+ DBUtil.get(clazz, DBUtil.value(obj), ",")+";";
		}
		return sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.ADD;
	}
}
