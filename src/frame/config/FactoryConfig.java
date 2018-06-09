package frame.config;

import java.util.HashMap;
import java.util.Map;

public abstract class FactoryConfig {
	protected Map<String, Class<?>> config = null;
	
	/**
	 * 初始化
	 */
	public FactoryConfig() {
		config = new HashMap<>();
		initConfig();
	}
	
	/**
	 * 子类应覆盖方法，将类初始化
	 */
	protected abstract void initConfig();
	public Map<String, Class<?>> getConfig() {
		if (null==config || 0==config.size()) {
			initConfig();
		}
		return config;
	}
	
	/**
	 * 得到对应的类对象
	 * @param name 初始化的name
	 * @return 对应类对象
	 */
	public Class<?> get(String name) {
		if (null==config || 0==config.size()) {
			initConfig();
		}
		return config.get(name);
	}
	
}
