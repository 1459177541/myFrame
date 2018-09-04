package config.properties;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public interface Configurable {

    default boolean loadProperties(String propertiesFileName){
        File file = new File(propertiesFileName);
        if (file.exists()) {
            return loadProperties(file);
        }else {
            return false;
        }
    }

    default boolean loadProperties(File propertiesFile){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertiesFile));
            return loadProperties(properties);
        } catch (IOException e) {
            return false;
        }
    }

    boolean loadProperties(Properties properties);

    default boolean saveProperties(String propertiesFileName){
        File file = new File(propertiesFileName);
        if (file.exists()) {
            return saveProperties(new File(propertiesFileName));
        }else {
            return false;
        }
    }

    boolean saveProperties(File propertiesFile);

}
