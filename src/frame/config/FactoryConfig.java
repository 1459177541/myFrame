package frame.config;

import java.util.HashMap;
import java.util.Map;

public interface FactoryConfig {
	static Map<String, Class<?>> config = new HashMap<>();
	
	public void initConfig();
	default public Map<String, Class<?>> getConfig() {
		return config;
	}
	default public Class<?> get(String name) {
		return config.get(name);
	}
	
}
