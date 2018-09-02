package config.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class AbstractConfigurationCenter implements Configurable{

    protected Map<Class<Configurable>, Properties> propertiesMap;
    protected Map<Class<Configurable>, String> fileMap;
    protected String defaultPropertiesFileName;

    public abstract void defaultInit();

    public Properties getProperties(Class<Configurable> configurableClass){
        if (null == propertiesMap){
            if (!loadPropertiaes(Objects.requireNonNull(defaultPropertiesFileName))){
                defaultInit();
            }
        }
        return propertiesMap.get(configurableClass);
    }

    public boolean setProperties(Configurable configurable){
        return configurable.loadProperties(getProperties((Class<Configurable>) configurable.getClass()));
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
                Class<Configurable> configurableClass = (Class<Configurable>) Class.forName((String)entry.getKey());
                propertiesMap.put(configurableClass,proper);
                fileMap.put(configurableClass, (String) entry.getValue());
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveProperties(File propertiesFile) {
        Properties properties = new Properties(propertiesMap.size());
        properties.putAll(fileMap.entrySet().stream().collect(Collectors.toMap(entry->entry.getKey().getName(),Map.Entry::getValue)));
        try {
            properties.store(new FileOutputStream(propertiesFile), "configuration center config");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveProperties(){
        return saveProperties(Objects.requireNonNull(defaultPropertiesFileName));
    }
}
