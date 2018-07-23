package dao.db.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(CONSTRUCTOR)
public @interface DB_column {
	/**
	 * 列名
	 */
	String columnName();
	
	/**
	 * 类型
	 */
	DB_columnType type();
	
	/**
	 * 长度
	 */
	int length() default 0;
	
	/**
	 * 主键
	 */
	boolean primaryKey() default false;
	
	/**
	 * 不为空
	 */
	boolean notNull() default false;
	
	/**
	 * 唯一键
	 */
	boolean uniqueIndex() default false;
	
	/**
	 * 二进制
	 */
	boolean binary() default false;
	
	/**
	 * 无符号位
	 */
	boolean unsignedData() default false;
	
	/**
	 * 自动增长
	 */
	boolean autoIncremental() default false;
}
