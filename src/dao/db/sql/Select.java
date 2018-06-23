package dao.db.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.db.annotation.DB_column;
import dao.util.DBUtil;

public class Select<T> extends Result<T> {

	private ResultSet rs;
	private ArrayList<T> result;
	
	@Override
	public String getSql() {
		if (null==obj) {
			return null;
		}
		if (null==sql) {
			String string = " *";
			if (null!=DBUtil.get(obj, DBUtil.key(), ",")) {
				string = DBUtil.get(obj, DBUtil.key(), ",");
			}
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
		if (null==connection||null==obj) {
			return false;
		}
		if (null==sql) {
			if (null==(sql=getSql())) {
				return false;
			}
		}
		if (null==statement) {
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
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
		if (null==result) {
			if (null==rs) {
				if (!execute()) {
					return null;
				}
			}
			Class<T> clazz = (Class<T>) obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			try {
				while (rs.next()) {
					T t = (T)new Object();
					boolean isAdd = false;
					for (Field field : fields) {
						String fieldName = field.getName();
						Method getMethod = clazz.getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
						if (null==getMethod.invoke(obj)) {
							break;
						}
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
			}
		}
		return result;
	}
	
	

}





