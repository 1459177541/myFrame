package frame.config;

import factory.BeanDefinition;
import frame.Single;

/**
 * 工厂配置类，完成工厂的配置设置
 * @author 杨星辰
 *
 */
public abstract class FactoryConfig extends Config<String, BeanDefinition>{

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
