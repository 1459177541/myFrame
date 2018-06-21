package factory;

import java.lang.reflect.Proxy;

import frame.proxy.ProxyHandlerByFactory;

/**
 * 代理工厂类，产生代理对象
 * @author 杨星辰
 *
 */
public class ProxyFactory extends ConfigFactory{
	
	private ConfigFactory handlerFactory;
	private ConfigFactory factory;
	
	public ProxyFactory(ConfigFactory factory) {
		this(factory,factory);
	}
	
	public ProxyFactory(ConfigFactory factory, ConfigFactory handlerFactory) {
		super(factory.getFactoryConfig());
		this.factory = factory;
		this.handlerFactory = handlerFactory;
	}

	public void setAction(ConfigFactory factory) {
		this.handlerFactory = factory;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> Object get(Class<T> clazz) {
		T o = (T) factory.get(clazz);
		ProxyHandlerByFactory<T> handler = new ProxyHandlerByFactory<>();
		handler.setTarget(o);
		handler.setFactory(handlerFactory);
		return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
	}

}
