package factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import frame.Autowired;
import config.FactoryConfig;

/**
 * 工厂类，通过配置类产生对象
 * @author 杨星辰
 *
 */
public class ConfigDefaultFactory extends ConfigFactory{


    public ConfigDefaultFactory(FactoryConfig factoryConfig) {
        super(factoryConfig);
    }

    @Override
    public BeanFactory setParentFactory(BeanFactory factory) {
        throw new IllegalArgumentException("这是默认的工厂，无父工厂");
    }

    /**
     * 通过类对象得到对应对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final Class<T> clazz) {
        T o = (T) setNumber(clazz);
        if (null!=o){
            return o;
        }else if (String.class.equals(clazz)){
            if (clazz.isAnnotationPresent(Autowired.class)){
                return (T)clazz.getAnnotation(Autowired.class).defaultString();
            }else {
                return (T)"";
            }
        }
        try {
            Constructor<T>[] constructor = (Constructor<T>[]) clazz.getConstructors();
            if (constructor.length == 0){
                System.out.println(clazz);
                throw new IllegalArgumentException("私有的构造方法");
            }
            Constructor<T> m;
            try {
                m = Arrays.stream(constructor)
                        .filter(c->c.isAnnotationPresent(Autowired.class))
                        .min(Comparator.comparing(c -> c.getAnnotation(Autowired.class).order()))
                        .get();
            }catch (NoSuchElementException e) {
                m = constructor[0];
            }
            o = m.newInstance(setParameter(m.getParameters()));
            autowiredMethod(o);
            autowireField(o);
        }catch (IllegalArgumentException e){
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * setter注入
     * @param obj 注入的对象
     */
    private <T> void autowiredMethod(T obj) {
        Arrays.stream(obj.getClass().getMethods())
                .filter(c->c.isAnnotationPresent(Autowired.class))
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(Autowired.class).order()))
                .forEach(e->{
                    try {
                        e.invoke(obj, setParameter(e.getParameters()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private <T> void autowireField(T obj) {
        final Class<T> c = (Class<T>) Objects.requireNonNull(obj).getClass();
        Stream.of(c.getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Autowired.class))
                .sorted(Comparator.comparingInt(a -> a.getAnnotation(Autowired.class).order()))
                .forEach(field->{
                    field.setAccessible(true);
                    String setterName = "set" + field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
                    try {
                        Method setter = c.getMethod(setterName, field.getType());
                        if (int.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultInt());
                        }else if (long.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultLong());
                        }else if (short.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultShort());
                        }else if (byte.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultByte());
                        }else if (double.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultDouble());
                        }else if (float.class.equals(field.getType())){
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultFloat());
                        }else if (String.class.equals(field.getType())) {
                            setter.invoke(obj, field.getAnnotation(Autowired.class).defaultString());
                        }else {
                            System.out.println();
                            setter.invoke(obj, get(field.getType()));
                        }
                    }catch (NoSuchMethodException e1){
                        //nothing
                    }catch (IllegalAccessException | InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                });
    }

    private Object[] setParameter(Parameter[] cs) {
        Object[] parameter = new Object[cs.length];
        for (int i = 0; i < cs.length; i++) {
            if (cs[i].isAnnotationPresent(Autowired.class)){
                if (int.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultInt();
                }else if (long.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultLong();
                }else if (short.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultShort();
                }else if (byte.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultByte();
                }else if (double.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultDouble();
                }else if (float.class.equals(cs[i].getType())){
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultFloat();
                }else if (String.class.equals(cs[i].getType())) {
                    parameter[i] = cs[i].getAnnotation(Autowired.class).defaultString();
                }else {
                    parameter[i] = get(cs[i].getAnnotation(Autowired.class).name());
                }
            }else {
                parameter[i] = Objects.requireNonNull(get(cs[i].getType()));
            }

        }
        return parameter;
    }

    private Object setNumber(Class type){
        if (Objects.requireNonNull(type).equals(int.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(long.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(short.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(byte.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(double.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(float.class)){
            return 0;
        }else if (Objects.requireNonNull(type).equals(String.class)) {
            return "";
        }else {
            return null;
        }
    }
}



