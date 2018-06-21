package dao.db.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;

import dao.db.annotation.DB_column;
import dao.db.annotation.DB_table;
import dao.util.Criteria;

public abstract class Result<T> {
	
	protected Connection connection;
	protected Statement statement;
	protected String sql;
	
	protected T obj;
	protected Criteria<T> criteria;
	
	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
	
	public void setCriteria(Criteria<T> criteria) {
		this.criteria = criteria;
	}

	protected abstract String getSql();
	public abstract boolean execute();
	
	
	@Override
	public String toString() {
		return getSql();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (null==obj) {
			return false;
		}
		if (!(obj instanceof Result)) {
			return false;
		}
		return this.getSql().equals(((Result<T>) obj).getSql());
	}

	@Override
	public int hashCode() {
		return obj.hashCode();
	}
	
	protected String getTableName() {
//		String string = null;
//		Class<? extends Object> clazz = obj.getClass();
//		if (clazz.isAnnotationPresent(DB_table.class)) {
//			string = clazz.getAnnotation(DB_table.class).tableName();
//		}
//		return string;
		return Objects.requireNonNull(obj.getClass().getAnnotation(DB_table.class)).tableName();
	}

	
	protected String get(String string,Object object, String link) {
		StringBuffer sql = new StringBuffer(" "+string+" (");
		boolean isAdd = false;
		if (obj!=null) {
			Class<? extends Object> clazz = obj.getClass();
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				field.setAccessible(true);
				try {
					if (field.getAnnotation(DB_column.class) == null) {
						continue;
					}
					String fName= field.getName();
					Method getMethod = clazz.getMethod("get"+fName.substring(0, 1).toUpperCase()+fName.substring(1));
					Object value = getMethod.invoke(obj);
					if (value!=null) {
						if (isAdd) {
							sql.append(" "+link+" ");
						}
						if (value instanceof String) {
							sql.append(fName+"='"+value+"'");
							isAdd = true;
						}else if(value instanceof Number){
							sql.append(fName+"="+value+"");
							isAdd = true;
						}
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		sql.append(")");
		if (!isAdd) {
			return "";
		}
		return sql.toString();
	}

	protected String getName() {
		if (obj==null) {
			return null;
		}
		StringBuffer sql = new StringBuffer(" (");
		boolean isAdd = false;
		Class<? extends Object> clazz = obj.getClass();
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.getAnnotation(DB_column.class) == null) {
					continue;
				}
				String fName= field.getName();
				Method getMethod = clazz.getMethod("get"+fName.substring(0, 1).toUpperCase()+fName.substring(1));
				Object value = getMethod.invoke(obj);
				if (value!=null) {
					if (isAdd) {
						sql.append(",");
					}
					sql.append(fName);
					isAdd = true;
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		sql.append(")");
		if (!isAdd) {
			return null;
		}
		return sql.toString();
	}

	protected String getValue() {
		StringBuffer sql = new StringBuffer(" (");
		boolean isAdd = false;
		if (obj!=null) {
			Class<? extends Object> clazz = obj.getClass();
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				field.setAccessible(true);
				try {
					if (field.getAnnotation(DB_column.class) == null) {
						continue;
					}
					String fName= field.getName();
					Method getMethod = clazz.getMethod("get"+fName.substring(0, 1).toUpperCase()+fName.substring(1));
					Object value = getMethod.invoke(obj);
					if (value!=null) {
						if (isAdd) {
							sql.append(",");
						}
						if (value instanceof String) {
							sql.append("'"+value+"'");
							isAdd = true;
						}else if(value instanceof Number){
							sql.append(value);
							isAdd = true;
						}
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		sql.append(")");
		if (!isAdd) {
			return "";
		}
		return sql.toString();
	}


}
