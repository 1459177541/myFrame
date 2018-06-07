package frame.config;

import java.util.HashMap;
import java.util.Map;

public abstract class FactoryConfig {
	protected Map<String, Class<?>> config = null;
	
	public FactoryConfig() {
		config = new HashMap<>();
		initConfig();
	}
	
	protected abstract void initConfig();
	public Map<String, Class<?>> getConfig() {
		if (null==config || 0==config.size()) {
			initConfig();
		}
		return config;
	}
	public Class<?> get(String name) {
		if (null==config || 0==config.size()) {
			initConfig();
		}
		return config.get(name);
	}
	
}
