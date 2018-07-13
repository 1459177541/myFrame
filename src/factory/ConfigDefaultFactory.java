package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import frame.Autowired;
import frame.config.FactoryConfig;

/**
 * 工厂类，通过配置类产生对象
 * @author 杨星辰
 *
 */
public class ConfigDefaultFactory extends ConfigFactory{
	
	
	public ConfigDefaultFactory(FactoryConfig factoryConfig) {
		super(factoryConfig);
	}
	
	/**
	 * 通过类对象得到对应对象
	 */
	@Override
	public <T> T get(final Class<T> clazz) {
		T o = null;
		try {
			@SuppressWarnings("unchecked")
			Constructor<T>[] constructor = (Constructor<T>[]) clazz.getConstructors();
			Constructor<T> m;
			try {
				m = Arrays.stream(constructor)
						.filter(c->c.isAnnotationPresent(Autowired.class))
						.min(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
						.get();
			}catch (NoSuchElementException e) {
				m = constructor[0];
			}
			o = m.newInstance(setParameter(m.getParameters()));
			autowiredMethod(o);
			autowireField(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * setter注入
	 * @param obj 注入的对象
	 */
	private <T> void autowiredMethod(T obj) {
		Arrays.stream(obj.getClass().getMethods())
			.filter(c->c.isAnnotationPresent(Autowired.class))
			.sorted(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
			.forEach(e->{
				try {
					if ("".equals(e.getAnnotation(Autowired.class).name()) && 1 == e.getParameterCount()) {
						e.invoke(obj, setParameter(e.getParameters()));
					}else {
						e.invoke(obj, get(e.getAnnotation(Autowired.class).name()));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		});
	}
	
	private <T> void autowireField(T obj) {
		Arrays.stream(obj.getClass().getFields())
			.filter(c->c.isAnnotationPresent(Autowired.class))
			.sorted(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
			.forEach(e->{
				e.setAccessible(true);
				Class<?> c = e.getClass();
				try {
					e.set(obj, get(c));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
	}
	
	private Object[] setParameter(Parameter[] cs) {
		Object[] parameter = new Object[cs.length];
		for (int i = 0; i < cs.length; i++) {
			if (cs[i].isAnnotationPresent(Autowired.class)){
				parameter[i] = get(cs[i].getAnnotation(Autowired.class).name());
			}else {
				parameter[i] = get(cs[i].getType());
			}
		}
		return parameter;
	}
}



