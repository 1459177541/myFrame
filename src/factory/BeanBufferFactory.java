package factory;

import dao.db.annotation.DB_table;
import dao.frame.Dao;
import dao.sf.annotation.SystemFile;
import dao.util.*;

import java.util.*;

public class BeanBufferFactory implements Factory<Class<?>, BeanBuffer<?>> {

    private static BeanBufferFactory root;

    private Map<Class<?>, BeanBuffer<?>> buffer = new HashMap<>();

    Dao dao;

    public BeanBufferFactory setDao(Dao dao) {
        this.dao = dao;
        return this;
    }

    public BeanBufferFactory setRoot(){
        root = this;
        return this;
    }

    @Override
    public BeanBuffer<?> get(Class<?> key) {
        buffer.put(key
                ,Objects.requireNonNullElseGet(
                        buffer.get(key)
                        ,()->Objects.requireNonNull(
                                Objects.requireNonNullElseGet(this.dao,()->{
                                    if (key.isAnnotationPresent(DB_table.class)){
                                        return new DatabaseDao();
                                    }else if (key.isAnnotationPresent(SystemFile.class)){
                                        return new FileStoreDao();
                                    }else {
                                        return null;
                                    }
                                }).load(key)
                        )
                ));
        if (this == root){
            return Objects.requireNonNull(buffer.get(key),"加载失败");
        }
        return Objects.requireNonNull(
                Objects.requireNonNullElseGet(
                        buffer.get(key)
                        ,()->Optional.of(root).map(p->p.get(key)).get())
                ,"加载失败"
        );
    }

}