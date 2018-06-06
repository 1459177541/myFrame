package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import frame.config.FactoryConfig;

public class BeanFactory implements Factory{
	/**
	 * 配置类
	 */
	private FactoryConfig factoryConfig;
	public BeanFactory() {
		this(null);
	}
	public BeanFactory(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
		factoryConfig.initConfig();
	}
	
	public FactoryConfig getFactoryConfig() {
		return factoryConfig;
	}
	public void setFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
		factoryConfig.initConfig();
	}
	
	/**
	 * 通过类名或者配置中的名得到对应对象
	 */
	@Override
	public Object get(final String name) {
		Class<?> c = factoryConfig.get(name);
		if(null==c) {
			try {
				c = Class.forName(name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return get(c);
	};
	
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
