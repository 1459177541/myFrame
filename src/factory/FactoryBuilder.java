package factory;

import java.util.Objects;

import frame.config.FactoryConfig;

/**
 * 工厂类的建造方法，默认开启代理与单例
 * @author 杨星辰
 *
 */
public class FactoryBuilder{
	
	private FactoryConfig factoryConfig;
	private ConfigFactory proxyActionFactory;
	private boolean isProxy;
	private boolean isSingle;
	
	public FactoryBuilder() {
		factoryConfig = null;
		proxyActionFactory = null;
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
		this.proxyActionFactory = proxyActionFactory;
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
	 * @param isProxy 
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setProxy(boolean isProxy) {
		this.isProxy = isProxy;
		return this;
	}

	
	
	/**
	 * 设置是否是需要检查单例
	 * @param isSingle
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setSingle(boolean isSingle) {
		this.isSingle = isSingle;
		return this;
	}

	
	
	/**
	 * 得到符合条件的工厂类
	 * @return
	 */
	public ConfigFactory get() {
		ConfigFactory f = null;
		f = new ConfigDefaultFactory(Objects.requireNonNull(factoryConfig, "没有配置类"));
		if (isSingle) {
			f = new SingleFactory(f);
		}
		if (isProxy) {
			f = new ProxyFactory(f);
			if (null == proxyActionFactory) {
				((ProxyFactory)f).setAction(new ConfigDefaultFactory(Objects.requireNonNull(factoryConfig, "没有 配置类")));
			}else {
				((ProxyFactory)f).setAction(proxyActionFactory);
			}
		}
		return f;
	}

}
