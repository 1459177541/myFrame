package factory;

import java.util.HashMap;
import java.util.Map;

import frame.Single;

public class SingleFactory extends ConfigFactory{
	
	private ConfigFactory factory;
	private Map<Class<?>,Object> singleMap;
	
	public SingleFactory(ConfigFactory factory) {
		super(factory.getFactoryConfig());
		this.factory = factory;
		singleMap = new HashMap<>();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if (!clazz.isAnnotationPresent(Single.class)) {
			return factory.get(clazz);
		}
		if (null!=singleMap.get(clazz)) {
			return (T) singleMap.get(clazz);
		}else {
			T t = factory.get(clazz);
			singleMap.put(clazz, t);
			return t;
		}
	}

}
