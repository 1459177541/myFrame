package factory;

import dao.db.annotation.DB_table;
import dao.frame.Dao;
import dao.sf.annotation.SystemFile;
import dao.util.*;

import java.util.*;

public class BeanBufferFactory implements Factory<Class<?>, BeanBuffer<?>> {

    private static BeanBufferFactory defaultFactory;

    private Map<Class<?>, BeanBuffer<?>> buffer = new HashMap<>();

    private Dao dao;

    public BeanBufferFactory setDao(Dao dao) {
        this.dao = dao;
        return this;
    }

    public BeanBufferFactory toDefault(){
        defaultFactory = this;
        return this;
    }

    @Override
    public BeanBuffer<?> get(Class<?> key) {
        if (null == buffer.get(key)) {
            BeanBuffer<?> beanBuffer = Objects.requireNonNullElseGet(this.dao, () -> {
                if (key.isAnnotationPresent(DB_table.class)) {
                    return new DatabaseDao();
                } else if (key.isAnnotationPresent(SystemFile.class)) {
                    return new FileStoreDao();
                } else {
                    return null;
                }
            }).load(key);
            if (null != beanBuffer){
                buffer.put(key,beanBuffer);
            }
        }
        if (this == defaultFactory){
            return Objects.requireNonNull(buffer.get(key),"加载失败");
        }
        return Objects.requireNonNull(
                Objects.requireNonNullElseGet(
                        buffer.get(key)
                        ,()->Optional.of(defaultFactory).map(p->p.get(key)).get())
                ,"加载失败"
        );
    }

}