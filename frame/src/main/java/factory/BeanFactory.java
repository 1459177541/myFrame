package factory;

public interface BeanFactory extends Factory<String, Object> {

    BeanFactory setParentFactory(BeanFactory factory);

    <T> Object get(Class<T> clazz);

}
