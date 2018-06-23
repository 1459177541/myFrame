package dao.db.sql;

import dao.util.DBUtil;

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
			sql="INSERT INTO "+DBUtil.getTableName(obj)+DBUtil.get(obj, DBUtil.key(), ",")+" "
					+ "VALUES"
					+ DBUtil.get(obj, DBUtil.value(obj), ",")+";";
		}
		return sql;
	}
}
