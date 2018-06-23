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
	 * @return
	 */
	String colmnName();
	
	/**
	 * 类型
	 * @return
	 */
	DB_columnType type();
	
	/**
	 * 长度
	 * @return
	 */
	int length() default 0;
	
	/**
	 * 主键
	 * @return
	 */
	boolean primaryKey() default false;
	
	/**
	 * 不为空
	 * @return
	 */
	boolean notNull() default false;
	
	/**
	 * 唯一键
	 * @return
	 */
	boolean uniqueIndex() default false;
	
	/**
	 * 二进制
	 * @return
	 */
	boolean binary() default false;
	
	/**
	 * 无符号位
	 * @return
	 */
	boolean unsignedData() default false;
	
	/**
	 * 自动增长
	 * @return
	 */
	boolean autoIncremental() default false;
}
