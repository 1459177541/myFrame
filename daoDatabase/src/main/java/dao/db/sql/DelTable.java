package dao.db.sql;

import dao.db.annotation.DB_table;
import dao.db.util.DBExecute;

import java.util.Objects;

public class DelTable<T> extends Update<T> {

	@Override
	protected String getSql() {
		if (null==obj) {
			return null;
		}
		if (null==sql) {
            sql = "DROP TABLE "
                    + Objects.requireNonNullElseGet(
                    clazz.getAnnotation(DB_table.class).tableName()
                    , () -> DBUtil.getTableName(obj)
            ) + ";";
		}
		return sql;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.DELETE_TABLE;
	}

}
