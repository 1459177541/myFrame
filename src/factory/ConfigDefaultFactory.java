package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
			if(0>=constructor.length) {
				o = clazz.newInstance();
			}else {
				Constructor<T> m = null;
				try {
					m = Arrays.asList(constructor).stream()
							.filter(c->c.isAnnotationPresent(Autowired.class))
							.min(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
							.get();
				}catch (NoSuchElementException e) {
					m = constructor[0];
				}
				o =(T) m.newInstance(setParameter(m.getParameterTypes()));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		autowiredMethod(o);
		autowireField(o);
		return o;
	}
	
	/**
	 * setter注入
	 * @param obj 注入的对象
	 */
	private <T> void autowiredMethod(T obj) {
		Arrays.asList(obj.getClass().getMethods()).stream()
			.filter(c->c.isAnnotationPresent(Autowired.class))
			.sorted(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
			.forEach(e->{
				try {
					e.invoke(obj, setParameter(e.getParameterTypes()));
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
		});
	}
	
	private <T> void autowireField(T obj) {
		Arrays.asList(obj.getClass().getFields()).stream()
			.filter(c->c.isAnnotationPresent(Autowired.class))
			.sorted(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
			.forEach(e->{
				e.setAccessible(true);
				Class<?> c = e.getClass();
				try {
					e.set(obj, get(c));
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			});
	}
	
	private Object[] setParameter(Class<?>[] cs) {
		Object[] parameter = new Object[cs.length];
		for (int i = 0; i < cs.length; i++) {
			parameter[i] = get(cs[i]);
		}	
		return parameter;
	}
}



