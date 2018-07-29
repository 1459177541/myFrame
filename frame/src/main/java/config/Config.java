package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public abstract class Config <K,V>{

	protected Map<K, V> config;
	
	protected Config<K,V> parent = null;

	protected String propertiesFileName;

	/*
	 * 初始化
	 */
	{
		config = new HashMap<>();
		initConfig();
	}

	/**
	 * 子类应覆盖方法，将配置初始化
	 */
	protected abstract void initConfig();

	protected abstract void initConfig(Properties properties);

    public abstract void saveConfig(String name) throws IOException;

	public void saveConfig() throws IOException{
	    saveConfig(Objects.requireNonNull(propertiesFileName,"未设置配置文件目录"));
    }

	public void initConfig(String propertiesFileName) throws IOException {
	    this.propertiesFileName = propertiesFileName;
	    Properties properties = new Properties();
	    properties.load(new FileInputStream(propertiesFileName));
	    initConfig(properties);
    }

    public void setPropertiesFileName(String name){
	    this.propertiesFileName = name;
    }

	/**
	 * 得到对应配置
	 * @param key 键 
	 * @return 值
	 */
	public V get(final K key) {
	    check();
	    return Objects.requireNonNull(
	            Objects.requireNonNullElseGet(
                    config.get(key)
                    ,()->Optional.of(parent).map(p->p.get(key)).get())
                ,"无相关配置"
        );
	}
	
	/**
	 * 设置父类,调用时优先从父类调用
	 * @param parent 父类
	 */
	public void setParent(Config<K,V> parent) {
		this.parent = parent;
	}

	/**
	 * 检查是否初始化
	 */
	protected void check() {
		if (null==config || 0==config.size()) {
			initConfig();
		}
	}

	public Map<K, V> getConfig() {
		check();
		return config;
	}

	/**
	 * 添加配置
	 * @param key 键
	 * @param value 值
	 */
	public void addConfig(K key, V value) {
		if (null == config){
		    initConfig();
        }
		this.config.put(key, value);
	}

	/**
	 * 添加配置
	 * @param config 配置对象
	 */
	public void addConfig(Config<K,V> config) {
		check();
		config.getConfig().forEach(this.config::put);
	}
}
