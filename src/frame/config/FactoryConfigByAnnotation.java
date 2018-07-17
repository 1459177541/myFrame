package frame.config;

import frame.Bean;

import java.util.Objects;

/**
 * 通过注解获取信息
 * 需通过子类获取添加的类
 *
 * @author 杨星辰
 */
public abstract class FactoryConfigByAnnotation extends FactoryConfig{

    protected <T> void add(Class<T> clazz){
        add(Objects.requireNonNull(clazz.getAnnotation(Bean.class)).name(), clazz);
    }

}
