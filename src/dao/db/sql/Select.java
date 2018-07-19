package dao.db.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import dao.db.annotation.DB_column;
import dao.db.util.DBExecute;
import dao.db.util.DBUtil;
import dao.db.util.GetConn;

public class Select<T> extends Result<T> {

	private ResultSet rs;
	private ArrayList<T> result;

	public Select(){
        connection = Objects.requireNonNullElseGet(connection, GetConn::getConnection);
    }



	@Override
	public String getSql() {
		if (null==sql) {
			String string = Objects.requireNonNullElse(DBUtil.get(clazz, DBUtil.key(), ","),"*");
			String where = "";
			if (null!=criteria) {
				where = "WHERE "+criteria.toString();
			}
			this.sql = "SELECT"+string+" "
					+ "FROM "+DBUtil.getTableName(obj)+" "
					+ where+";";
		}
		return sql;
	}

	@Override
	public boolean execute() {
		if (!check()){
			return false;
		}
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> getResult(){
		if (null!=result) {
			return result;
		}
		if (null==rs) {
			if (!execute()) {
				return null;
			}
		}
		Class<T> clazz = Objects.requireNonNullElseGet(this.clazz,()->(Class<T>) obj.getClass());
		Field[] fields = clazz.getDeclaredFields();
		try {
			while (rs.next()) {
				T t = (T)new Object();
				boolean isAdd = false;
				for (Field field : fields) {
					String fieldName = field.getName();
					if (field.isAnnotationPresent(DB_column.class)) {
						Method setMethod = clazz.getMethod("set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
						setMethod.invoke(t, rs.getObject(field.getDeclaredAnnotation(DB_column.class).colmnName()));
						isAdd = true;
					}
				}
				if (isAdd) {
					result.add(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public DBExecute getState() {
		return DBExecute.SELECT;
	}

}

