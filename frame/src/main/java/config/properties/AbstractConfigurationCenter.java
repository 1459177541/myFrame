package config.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


public abstract class AbstractConfigurationCenter implements Configurable{

    protected Map<String, Properties> propertiesMap;
    protected Map<String, String> fileMap;
    protected String defaultPropertiesFileName;

    public abstract void defaultInit();

    public Properties getProperties(String configName){
        check();
        return Objects.requireNonNull(propertiesMap.get(configName), "未设置"+configName+"的配置");
    }

    public boolean setProperties(Configurable configurable, String configName){
        check();
        return configurable.loadProperties(getProperties(configName));
    }

    private void check(){
        if (null == propertiesMap){
            if (!loadProperties(Objects.requireNonNull(defaultPropertiesFileName, "未设置默认配置文件"))){
                defaultInit();
            }
        }
    }

    @Override
    public boolean loadProperties(Properties properties) {
        propertiesMap = new HashMap<>();
        fileMap = new HashMap<>();
        Properties proper;
        for (Map.Entry entry :
                properties.entrySet()) {
            proper = new Properties();
            try {
                proper.load(new FileInputStream((String)entry.getKey()));
                propertiesMap.put((String) entry.getKey(),proper);
                fileMap.put((String) entry.getKey(), (String) entry.getValue());
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveProperties(File propertiesFile) {
        Properties properties = new Properties(propertiesMap.size());
        properties.putAll(fileMap);
        try {
            properties.store(new FileOutputStream(propertiesFile), "configuration center config");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveProperties(){
        return saveProperties(Objects.requireNonNull(defaultPropertiesFileName, "未设置默认配置文件"));
    }
}
