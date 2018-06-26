package factory;

import java.lang.reflect.Proxy;
import java.util.Objects;

import build.ProxyHandlerBuild;
import frame.proxy.ProxyHandler;

/**
 * 代理工厂类，产生代理对象
 * @author 杨星辰
 *
 */
public class ProxyFactory extends ConfigFactory{
	
	private ConfigFactory factory;
	private ProxyHandlerBuild build;

	public ProxyFactory(ConfigFactory factory) {
		this(factory,factory);
	}

	/**
	 * @param factory bean工厂
	 * @param handlerFactory AOP工厂
	 */
	public ProxyFactory(ConfigFactory factory, ConfigFactory handlerFactory) {
		super(factory.getFactoryConfig());
		this.factory = factory;
		build = new ProxyHandlerBuild().setConfigFactory(handlerFactory);
	}

	public ProxyFactory setHandlerBuild(ProxyHandlerBuild handlerBuild){
		this.build = handlerBuild;
		return this;
	}

	public ProxyHandlerBuild getHandlerBuild() {
		return build;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Object get(Class<T> clazz) {
		T o = (T) factory.get(clazz);
		ProxyHandler<T> handler = (ProxyHandler<T>) Objects.requireNonNull(build).get();
		handler.setTarget(o);
		return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
	}

}
