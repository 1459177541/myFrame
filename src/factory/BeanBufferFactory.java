package factory;

import dao.db.annotation.DB_table;
import dao.frame.Dao;
import dao.sf.annotation.SystemFile;
import dao.util.*;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

public class BeanBufferFactory implements Factory<Class<?>, BeanBuffer<?>> {

    private static BeanBufferFactory defaultFactory;

    /**
     * 使用map保存class及对应的缓存的软引用
     * 保证可以快速加载数据且使用软引用减少内存开销
     */
    private Map<Class<?>, SoftReference<BeanBuffer<?>>> buffer = new HashMap<>();

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
        if (this == defaultFactory){
            return Objects.requireNonNullElseGet(buffer.get(key).get(),()->load(key));
        }
        return Objects.requireNonNull(
                Objects.requireNonNullElseGet(
                        buffer.get(key).get()
                        ,()->Optional.of(defaultFactory).map(p->p.get(key)).get())
                ,"加载失败"
        );
    }

    @SuppressWarnings({"unchecked", "UnusedAssignment"})
    private synchronized <T> BeanBuffer<T> load(Class<T> key){
        if (null == buffer.get(key) || null == buffer.get(key).get()) {
            BeanBuffer<T> beanBuffer = Objects.requireNonNullElseGet(this.dao, () -> {
                if (key.isAnnotationPresent(DB_table.class)) {
                    return new DatabaseDao();
                } else if (key.isAnnotationPresent(SystemFile.class)) {
                    return new FileStoreDao();
                } else {
                    return null;
                }
            }).load(key);
            if (null != beanBuffer){
                SoftReference<BeanBuffer<?>> bufferSoftReference = new SoftReference<>(beanBuffer);
                buffer.put(key,bufferSoftReference);
                beanBuffer = null;
            }else {
                throw new NullPointerException("加载失败");
            }
        }
        return Objects.requireNonNullElseGet((BeanBuffer<T>) buffer.get(key).get(),()->load(key));
    }

}