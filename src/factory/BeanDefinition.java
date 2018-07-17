package factory;


public class BeanDefinition<T> {

    private String name;

    private Class<T> beanClass;

    private String className;

    private boolean isSingle;

    public BeanDefinition() {

    }

    public BeanDefinition(String name, Class<T> beanClass, boolean isSingle) {
        setName(name);
        setBeanClass(beanClass);
        setSingle(isSingle);
    }

    public String getName() {
        return name;
    }

    public BeanDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public BeanDefinition setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
        className = beanClass.getName();
        return this;
    }

    public String getClassName() {
        return className;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public BeanDefinition setSingle(boolean single) {
        isSingle = single;
        return this;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
