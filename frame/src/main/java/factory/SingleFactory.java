package factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import frame.Single;

public class SingleFactory implements BeanFactoryHandler {
	
//	private BeanFactory factory;
	private static Map<Class<?>,Object> singleMap;
	
	static {
		singleMap = new HashMap<>();
	}

	public SingleFactory(){}

	@SuppressWarnings("unchecked")
    @Override
	public synchronized <T> T get(Class<T> clazz, Object obj) {
		if (!clazz.isAnnotationPresent(Single.class)) {
			return (T)Objects.requireNonNull(obj);
		}
		return (T)singleMap.computeIfAbsent(clazz, arg->Objects.requireNonNull(obj));
	}

    @Override
    public BeanFactoryHandler setBeanFactory(BeanFactory factory) {
//        this.factory = factory;
        return this;
    }
}
