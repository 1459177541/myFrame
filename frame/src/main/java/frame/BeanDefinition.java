package frame;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BeanDefinition<T> {

    private String name;

    private Class<T> beanClass;

    private String className;

    private boolean isSingle;

    public BeanDefinition() {

    }

    /**
     * 通过路径设置bean定义
     * @param name bean的名字
     * @param path 项目文件夹的路径，不含包路径
     * @param className 类的全限定名
     * @param isSingle 是否单例
     */
    @SuppressWarnings("unchecked")
    public BeanDefinition(String name, String path, String className, boolean isSingle){
        setName(name);
        setSingle(isSingle);
        try {
            setBeanClass(path,className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过路径设置bean定义
     * @param name bean的名字
     * @param beanClass bean的class对象
     * @param isSingle 是否单例
     */
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

    @SuppressWarnings("unchecked")
    public BeanDefinition setBeanClass(String path, String className) throws ClassNotFoundException {
        this.beanClass = (Class<T>) new FileClassLoader(path).loadClass(className);
        this.className = className;
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

    private class FileClassLoader extends ClassLoader{

        private Path path;

        public FileClassLoader(String path) {
                this.path = Paths.get(path);
        }

        public FileClassLoader(Path path) {
            this.path = path;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            path = path.resolve(name.replace(',', File.separatorChar) + ".class");
            byte[] classData;
            try {
                classData = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ClassNotFoundException(path.toString());
            }
            return defineClass(name, classData, 0, classData.length);
        }
    }

}
