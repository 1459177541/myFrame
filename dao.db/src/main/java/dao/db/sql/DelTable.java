package dao.db.sql;

import dao.db.util.DBExecute;
import dao.db.util.DBUtil;

public class DelTable<T> extends Update<T> {

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		if (null==sql) {
			sql="DROP TABLE "+DBUtil.getTableName(obj)+";";
		}
		return sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.DELETE_TABLE;
	}

}
