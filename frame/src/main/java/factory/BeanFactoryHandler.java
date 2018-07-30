package factory;

public interface BeanFactoryHandler{

    BeanFactoryHandler setBeanFactory(BeanFactory factory);

    <T> Object get(Class<T> clazz, Object obj);

}
