package factory;

import java.util.HashMap;
import java.util.Map;

import frame.Single;

public class SingleFactory extends ConfigFactory{
	
	private ConfigFactory factory;
	private static Map<Class<?>,Object> singleMap;
	
	static {
		singleMap = new HashMap<>();
	}
	
	public SingleFactory(ConfigFactory factory) {
		super(factory.getFactoryConfig());
		this.factory = factory;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T> T get(Class<T> clazz) {
		if (!clazz.isAnnotationPresent(Single.class)) {
			return (T) factory.get(clazz);
		}
		return (T) singleMap.computeIfAbsent(clazz, factory::get);
	}

}
