package dao.db.sql;

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
			sql="INSERT INTO "+getTableName()+getName()+" "
					+ "VALUES"
					+ getValue()+";";
		}
		return sql;
	}
}
