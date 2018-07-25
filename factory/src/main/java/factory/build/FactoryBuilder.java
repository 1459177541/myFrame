package factory.build;


import config.FactoryConfig;
import factory.BeanFactory;
import factory.ConfigDefaultFactory;
import util.Build;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * 工厂类的建造方法，默认开启代理与单例
 * @author 杨星辰
 *
 */
public class FactoryBuilder implements Build<BeanFactory> {

	private FactoryConfig factoryConfig;

	public FactoryBuilder() {
		factoryConfig = null;
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
	 * 设置配置类
	 * @param factoryConfig 配置类对象
	 * @return 自身,以便链式操作
	 */
	public FactoryBuilder setFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
		return this;
	}
	
	/**
	 * 得到符合条件的工厂类
	 * @return 工厂类
	 */
	@Override
	public BeanFactory build() {
	    BeanFactory factory = null;
	    if (factoryConfig!=null) {
            factory = new ConfigDefaultFactory(Objects.requireNonNull(factoryConfig, "没有配置类"));
        }
        return ServiceLoader.load(BeanFactory.class).stream()
                .map(ServiceLoader.Provider::get)
                .reduce(Objects.requireNonNull(factory),((beanFactory, e) -> e.setParentFactory(beanFactory)));
	}

}
