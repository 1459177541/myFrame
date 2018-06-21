package dao.db.sql;

public class DelTable<T> extends Update<T> {

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		if (null==sql) {
			sql="DROP TABLE "+getTableName()+";";
		}
		return sql;
	}

}
