package factory;

import config.FactoryConfig;

import java.util.Objects;

public abstract class ConfigFactory implements Factory<String, Object> {
	/**
	 * 配置类
	 */
	protected FactoryConfig factoryConfig;
	
	public ConfigFactory(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
	}
	
	public FactoryConfig getFactoryConfig() {
		return factoryConfig;
	}
	public void setFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig = factoryConfig;
	}
	public void addFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig.addConfig(factoryConfig);
	}
	
	/**
	 * 通过类名或者配置中的名得到对应对象
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Object get(final String name) {
		return get(Objects.requireNonNull(factoryConfig).get(name).getBeanClass());
	}
	
	public abstract <T> Object get(Class<T> clazz);
}
