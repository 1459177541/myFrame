package dao.service;

import config.properties.Configurable;
import dao.beanBuffer.BeanBuffer;

public interface Dao extends Configurable {

    void save(BeanBuffer beanBuffer);

    <T> BeanBuffer<T> load(Class<T> clazz);

    <T> boolean isCanLoad(Class<T> clazz);

}
