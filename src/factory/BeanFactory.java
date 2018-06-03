package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanFactory implements Factory{
	private final Factory parent;
	public BeanFactory() {
		this(null);
	}
	public BeanFactory(Factory factory) {
		this.parent = factory;
	}
	public Factory getParentFactory() {
		return parent;
	}
	@Override
	public Object get(final String name) {
		Object o = null;
		try {
			Class<?> c = Class.forName(name);
			o = get(c);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	};
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> clazz) {
		T o = null;
		try {
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
