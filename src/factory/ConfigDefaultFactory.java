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
				return clazz.newInstance();
			}
			Constructor<T> m = null;
			try {
				m = Arrays.asList(constructor).stream()
						.filter(c->c.isAnnotationPresent(Autowired.class))
						.min(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
						.get();
			}catch (NoSuchElementException e) {
				m = constructor[0];
			}
			Class<?>[] cs = m.getParameterTypes();
			Object[] parameter = new Object[cs.length];
			for(int i = 0 ; i < cs.length ; i++) {
				parameter[i] = get(cs[i]);
			}
			o =(T) m.newInstance(parameter);
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
		return o;
	}
	
	
	private <T> void autowiredMethod(T obj) {
		Arrays.asList(obj.getClass().getMethods()).stream()
			.filter(c->c.isAnnotationPresent(Autowired.class))
			.forEach(e->{
				Class<?>[] cs = e.getParameterTypes();
				Object[] parameter = new Object[cs.length];
				for (int i = 0; i < cs.length; i++) {
					parameter[i] = get(cs[i]);
				}
				try {
					e.invoke(obj, parameter);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
		});
		
	}
}
