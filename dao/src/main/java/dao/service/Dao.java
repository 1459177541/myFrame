package dao.service;

import dao.beanBuffer.BeanBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public interface Dao {

    void save(BeanBuffer beanBuffer);

    <T> BeanBuffer<T> load(Class<T> clazz);

    <T> boolean isCanLoad(Class<T> clazz);

    default void setProperties(String propertiesFileName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(Objects.requireNonNull(propertiesFileName)));
        setProperties(properties);
    }

    void setProperties(Properties properties) ;

    void saveProperties(String propertiesFileName) throws IOException ;

}
