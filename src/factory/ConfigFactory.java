package factory;

import frame.config.FactoryConfig;

public abstract class ConfigFactory implements Factory<String, Object>{
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
	public void addFactoryConfig(String name,Class<?> clazz) {
		this.factoryConfig.addConfig(name, clazz);
	}
	public void addFactoryConfig(FactoryConfig factoryConfig) {
		this.factoryConfig.addConfig(factoryConfig);
	}
	
	/**
	 * 通过类名或者配置中的名得到对应对象
	 */
	@Override
	public Object get(final String name) {
		Class<?> c = factoryConfig.get(name);
		if(null==c) {
			try {
				c = Class.forName(name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return get(c);
	};
	
	public abstract <T> Object get(Class<T> clazz);
}
