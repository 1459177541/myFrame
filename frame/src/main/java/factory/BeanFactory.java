package factory;

public interface BeanFactory extends Factory<String, Object> {

    <T> Object get(Class<T> clazz);

    void addBeanFactoryHandler(BeanFactoryHandler beanFactoryHandler);

}
