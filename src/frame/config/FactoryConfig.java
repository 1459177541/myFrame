package frame.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂配置类，完成工厂的配置设置
 * @author 杨星辰
 *
 */
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
	 * 子类应覆盖方法，将配置初始化
	 */
	protected abstract void initConfig();
	public Map<String, Class<?>> getConfig() {
		check();
		return config;
	}
	
	/**
	 * 添加配置
	 * @param name 
	 * @param clazz
	 */
	public void addConfig(String name, Class<?> clazz) {
		check();
		this.config.put(name, clazz);
	}
	
	/**
	 * 添加配置
	 * @param config 配置对象
	 */
	public void addConfig(FactoryConfig config) {
		check();
		config.getConfig().forEach(this.config::put);
	}
	
	/**
	 * 得到对应的类对象
	 * @param name 初始化的name
	 * @return 对应类对象
	 */
	public Class<?> get(String name) {
		check();
		return config.get(name);
	}
	
	private void check() {
		if (null==config || 0==config.size()) {
			initConfig();
		}
	}
	
}
