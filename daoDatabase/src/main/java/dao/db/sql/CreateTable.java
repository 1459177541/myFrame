package dao.db.sql;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dao.db.annotation.DB_column;
import dao.db.annotation.DB_table;
import dao.db.util.DBExecute;

public class CreateTable<T> extends Update<T> {

	private boolean isAdd;
	@Override
	protected String getSql() {
		if (null==this.sql) {
			StringBuffer sql = new StringBuffer("CREATE TABLE ");
            Class<?> clazz = Objects.requireNonNullElseGet(this.clazz,obj::getClass);
            sql.append(clazz.getAnnotation(DB_table.class).tableName()).append("(");
			isAdd = false;
			Stream<Field> stream = Stream.of(clazz.getDeclaredFields());
			stream.filter(f->f.isAnnotationPresent(DB_column.class))
				.forEach(f->{
					DB_column annotation = f.getAnnotation(DB_column.class);
					if(isAdd) {
						sql.append(",");
					}
					sql.append("`").append(annotation.columnName()).append("` ");
					sql.append(annotation.type().name());
					if (0!=annotation.length()) {
						sql.append("(").append(annotation.length()).append(")");
					}
					if(annotation.unsignedData()) {
						sql.append(" UNSIGNED");
					}
					if(annotation.notNull()) {
						sql.append(" NOTNULL");
					}
					if(annotation.autoIncremental()) {
						sql.append(" AUTO_INCREMENT");
					}
					isAdd = true;
				});
			List<Field> fields = stream.filter(f->f.isAnnotationPresent(DB_column.class))
				.filter(f->f.getAnnotation(DB_column.class).primaryKey())
				.collect(Collectors.toList());
			if (fields.size()>1) {
				throw new RuntimeException("多个主键");
			}else if(fields.size() == 1) {
				sql.append("PRIMARY KEY (`").append(fields.get(0).getAnnotation(DB_column.class).columnName()).append("`)");
			}
			sql.append(");");
			this.sql=sql.toString();
		}
		return sql;
	}
	@Override
	public DBExecute getState() {
		return DBExecute.CREATE_TABLE;
	}

}
