package build;

import factory.ConfigDefaultFactory;
import factory.ConfigFactory;
import factory.ProxyFactory;
import factory.SingleFactory;
import frame.config.FactoryConfig;

import java.util.Objects;

/**
 * 工厂类的建造方法，默认开启代理与单例
 * @author 杨星辰
 *
 */
public class FactoryBuilder implements Build<ConfigFactory> {

	private FactoryConfig factoryConfig;
	private boolean isSingle;
	private boolean isProxy;
	private ProxyHandlerBuild proxyHandlerBuild;

	public FactoryBuilder() {
		proxyHandlerBuild = new ProxyHandlerBuild();
		factoryConfig = null;
		isProxy = true;
		isSingle = true;
	}
	
	/**
	 * 
	 * @param factoryConfig 配置类对象
	 */
	public FactoryBuilder(FactoryConfig factoryConfig) {
		this();
		this.factoryConfig = factoryConfig;
	}

	
	/**
	 * 设置代理对象的AOP工厂类
	 * @param proxyActionFactoryConfig 配置类对象
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setProxyActionFactoryConfig(FactoryConfig proxyActionFactoryConfig) {
		setProxyActionFactoryConfig(new ConfigDefaultFactory(proxyActionFactoryConfig));
		return this;
	}
	
	
	/**
	 * 设置代理对象的AOP工厂类
	 * @param proxyActionFactory AOP工厂类
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setProxyActionFactoryConfig(ConfigFactory proxyActionFactory) {
		proxyHandlerBuild.setConfigFactory(proxyActionFactory);
		setProxy(true);
		return this;
	}

	
	/**
	 * 设置配置类
	 * @param factoryConfig 配置类对象
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
		return this;
	}
	
	
	/**
	 * 设置是否需要代理
	 * @param isProxy 是否需要代理
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setProxy(boolean isProxy) {
		this.isProxy = isProxy;
		return this;
	}



	/**
	 * 设置是否是需要检查单例
	 * @param isSingle 是否检查单例
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setSingle(boolean isSingle) {
		this.isSingle = isSingle;
		return this;
	}


	/**
	 * 设置是否使用异步，默认开启
	 * @param isAsync 是否检查异步
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setAsync(boolean isAsync){
		proxyHandlerBuild.setAsync(isAsync);
		isProxy = true;
		return this;
	}



	/**
	 * 得到符合条件的工厂类
	 * @return 工厂类
	 */
	@Override
	public ConfigFactory get() {
		ConfigFactory f = new ConfigDefaultFactory(Objects.requireNonNull(factoryConfig, "没有配置类"));
		if (isSingle) {
			f = new SingleFactory(f);
		}
		if (isProxy) {
			f = new ProxyFactory(f);
			((ProxyFactory)f).setHandlerBuild(proxyHandlerBuild);
		}
		return f;
	}

}
