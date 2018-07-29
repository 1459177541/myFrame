package factory.factory;


import dao.beanBuffer.BeanBuffer;
import dao.service.Dao;
import factory.BeanFactory;
import factory.Factory;
import factory.build.DaoBuild;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BeanBufferFactory implements Factory<Class<?>, BeanBuffer<?>> {

    private static BeanBufferFactory defaultFactory;

    private BeanFactory beanFactory;

    /**
     * 使用map保存class及对应的缓存的软引用
     * 保证可以快速加载数据且使用软引用减少内存开销
     */
    private Map<Class<?>, SoftReference<BeanBuffer<?>>> buffer = new HashMap<>();

    private Map<Class<?>, Dao> dao;

    public BeanBufferFactory(){
        dao = new HashMap<>();
    }

    public BeanBufferFactory setDao(Class clazz,Dao dao) {
        this.dao.put(clazz,dao);
        return this;
    }

    public Dao getDao(Class clazz){
        return Objects.requireNonNullElseGet(dao.get(clazz),()->dao.put(clazz,DaoBuild.getDao(clazz)));
    }

    public BeanBufferFactory toDefault(){
        defaultFactory = this;
        return this;
    }

    public BeanBufferFactory setBeanFactory(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
        return this;
    }

    public BeanBuffer<?> get(String name){
        return get((Class<?>) Objects.requireNonNull(beanFactory.get(name),"未设置工厂类"));
    }

    @Override
    public BeanBuffer<?> get(Class<?> key) {
        if (this == defaultFactory || null == defaultFactory){
            return Objects.requireNonNullElseGet(
                    Optional.ofNullable(buffer.get(key)).map(SoftReference::get).orElse(null)
                    ,()->load(key));
        }
        return Objects.requireNonNull(
                Objects.requireNonNullElseGet(
                        Optional.ofNullable(buffer.get(key)).map(SoftReference::get).orElse(null)
                        ,()->Optional.ofNullable(defaultFactory).map(p->p.get(key)).orElse(null))
                ,"加载失败"
        );
    }

    @SuppressWarnings({"unchecked", "UnusedAssignment"})
    private synchronized <T> BeanBuffer<T> load(Class<T> key){
        if (null == buffer.get(key) || null == buffer.get(key).get()) {
            BeanBuffer<T> beanBuffer = Objects.requireNonNullElseGet(this.dao.get(key), () -> dao.put(key, DaoBuild.getDao(key))).load(key);
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