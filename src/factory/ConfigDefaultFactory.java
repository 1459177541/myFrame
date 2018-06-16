package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
			Constructor<T> m = constructor[0];
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
		return o;
	}
}
