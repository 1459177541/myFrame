package dao.service;

import dao.beanBuffer.BeanBuffer;

public interface Dao {

    void save(BeanBuffer beanBuffer);

    <T> BeanBuffer<T> load(Class<T> clazz);

    <T> boolean isCanLoad(Class<T> clazz);

}
