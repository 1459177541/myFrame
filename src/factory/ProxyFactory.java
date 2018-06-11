package factory;

import java.lang.reflect.Proxy;

import frame.config.FactoryConfig;
import frame.proxy.ProxyHandlerByFactory;

/**
 * 代理工厂类，产生代理对象
 * @author 杨星辰
 *
 */
public class ProxyFactory extends BeanFactory{
	
	private Factory handlerFactory;
	
	public ProxyFactory() {
		super();
	}
	
	public ProxyFactory(FactoryConfig factoryConfig) {
		super(factoryConfig);
		handlerFactory = new BeanFactory(factoryConfig);
	}

	public void setAction(Factory factory) {
		this.handlerFactory = factory;
	}
	
	public void setAction(FactoryConfig config) {
		this.handlerFactory = new BeanFactory(config);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		T o = super.get(clazz);
		ProxyHandlerByFactory<T> handler = new ProxyHandlerByFactory<>();
		handler.setTarget(o);
		handler.setFactory(handlerFactory);
		return (T)Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
	}
}
