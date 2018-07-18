package frame.config;

import factory.BeanDefinition;
import frame.Single;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

/**
 * 工厂配置类，完成工厂的配置设置
 * @author 杨星辰
 *
 */
public abstract class FactoryConfig extends Config<String, BeanDefinition>{

    protected static volatile FactoryConfig root = null;

    public synchronized void setRoot(){
        root = this;
    }

    @Override
    public BeanDefinition get(final String key) {
        if (root == this){
            return Objects.requireNonNull(config.get(key),"无相关配置");
        }else{
            check();
            return Objects.requireNonNull(
                    Objects.requireNonNullElseGet(
                            config.get(key)                                         //如果当前有则从当前返回
                            ,()->Optional.of(
                                    Objects.requireNonNullElseGet(parent, ()->root) //否则检查父对象
                            ).map(fc->fc.get(key)).get()                            //有则从父对象返回，无则从根对象返回
                    ),"无相关配置"
            );
        }
    }

    protected <T> void add(String name, Class<T> clazz){
        BeanDefinition<T> beanDefinition = new BeanDefinition<>();
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setName(name);
        boolean isSingle = false;
        if (clazz.isAnnotationPresent(Single.class)) {
            isSingle = true;
        }
        beanDefinition.setSingle(isSingle);
        config.put(name, beanDefinition);
    }

}
