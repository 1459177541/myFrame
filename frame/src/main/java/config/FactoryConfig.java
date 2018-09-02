package config;

import frame.Single;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 工厂配置类，完成工厂的配置设置
 * @author 杨星辰
 *
 */
public abstract class FactoryConfig extends Config<String, BeanDefinition> {

    protected static volatile FactoryConfig root = null;

    public synchronized void setRoot(){
        root = this;
    }

    @Override
    public BeanDefinition get(final String key) {
        check();
        if (root == this){
            return Objects.requireNonNull(config.get(key),"无相关配置");
        }else{
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

    /**
     * 添加信息
     * @param name bean名
     * @param clazz bean class
     * @param <T> bean类型
     */
    protected <T> void add(String name, Class<T> clazz){
        BeanDefinition<T> beanDefinition = new BeanDefinition<>();
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setName(name);
        beanDefinition.setSingle(clazz.isAnnotationPresent(Single.class));
        addConfig(name, beanDefinition);
    }

    /**
     * 添加信息
     * @param name bean名
     * @param classPath bean所在项目目录
     * @param className bean的全限定类名
     * @throws ClassNotFoundException 找不到目标文件抛出
     */
    protected void add(String name, String classPath, String className) throws ClassNotFoundException {
        BeanDefinition beanDefinition = new BeanDefinition<>();
        beanDefinition.setBeanClass(classPath,className);
        beanDefinition.setName(name);
        beanDefinition.setSingle(beanDefinition.getBeanClass().isAnnotationPresent(Single.class));
        addConfig(name, beanDefinition);
    }

    @Override
    public boolean loadProperties(Properties properties) {
//        properties.forEach((name,clazz)-> {
//            try {
//                add((String) name,Class.forName((String) clazz));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        });
        for (Map.Entry entry : properties.entrySet()) {
            try {
                add((String)entry.getKey(),Class.forName((String)entry.getValue()));
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveProperties(File propertiesFile) {
        Properties properties = new Properties(config.size());
        properties.putAll(config.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getClassName())));
        try {
            properties.store(new FileOutputStream(propertiesFile),"工厂文件注释信息");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
