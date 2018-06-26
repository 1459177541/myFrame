package dao.db.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import dao.db.annotation.DB_column;
import dao.db.annotation.DB_table;

public class DBUtil {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private DBUtil() {};
	
	public static String getTableName(Object obj) {
		return Objects.requireNonNull(obj.getClass().getAnnotation(DB_table.class)).tableName();
	}
	
	public static <T> String get(T t, Function<Field, String> mapper, String link) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) t.getClass();
		return Arrays.asList(clazz.getDeclaredFields()).stream()
			.filter(f->f.isAnnotationPresent(DB_column.class) && null!=value(t))
			.map(mapper)
			.collect(Collectors.joining(" "+link+" ", " (", ") "));
	}
	
	public static Function<Field, String> key(){
		return f->{
			f.setAccessible(true);
			return "`"+f.getAnnotation(DB_column.class).colmnName()+"`";
		};
	}
	
	public static Function<Field, String> value(Object object){
		return f->{
			f.setAccessible(true);
			try {
				Object value = f.get(object);
				switch (f.getAnnotation(DB_column.class).type()) {
				case TINYINT:
				case SMALLINT:
				case MEDIUMINT:
				case INT:
				case BIGINT:
				case FLOAT:
				case DOUBLE:
					return value.toString();
				case CHAR:
				case VARCHAR:
				case TINYTEXT:
				case TEXT:
				case MEDIUMTEXT:
				case LONGTEXT:
				case TINTBLOB:
				case BLOB:
				case MEDIUMBLOB:
				case LONGBLOB:
					return "'"+value+"'";
				case DATE:
				case TIME:
				case YEAR:
				case DATETIME:
				case TIMESTAMP:
					return sdf.format((Date)value);
				default:
					break;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		};
	}
	
	public static Function<Field, String> kv(Object obj){
		return f->{
			return key().apply(f)+"="+value(obj).apply(f);
		};
	}
	
}
