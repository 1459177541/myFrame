package frame;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记工厂类使用的构造方法,或者自动注入的方法
 * @author 杨星辰
 *
 */
@Retention(RUNTIME)
@Target({CONSTRUCTOR,METHOD,FIELD})
public @interface Autowired {
	int order() default 0;
}
