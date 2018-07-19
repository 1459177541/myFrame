package dao.frame;

import dao.util.BeanBuffer;
import dao.util.FileStoreDao;

public interface Dao {

    void save(BeanBuffer beanBuffer);

    <T> BeanBuffer<T> load(Class<T> clazz);

}
