package proxy.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.stream.Stream;

import config.FactoryConfig;
import factory.ConfigDefaultFactory;
import factory.ConfigFactory;
import proxy.action.AfterReturnAction;
import proxy.action.BeforeAction;
import proxy.action.CheckAction;
import proxy.action.ThrowsExceptionAction;
import proxy.annotation.AfterReturn;
import proxy.annotation.Before;
import proxy.annotation.Check;
import proxy.annotation.ThrowsException;

/**
 *
 * 通过工厂配置文件找到代理方法
 *
 * @author 杨星辰
 *
 * @param <T>
 */
public class AopProxyHandler<T> extends DefaultProxyHandler<T> {

    public AopProxyHandler(ProxyHandler<T> parent) {
        super(parent);
    }

    private ConfigFactory factory;

    public void setFactory(ConfigFactory factory) {
        this.factory = factory;
    }

    public void setConfig(FactoryConfig config) {
        factory = new ConfigDefaultFactory(config);
    }

    private boolean isExecute = true;
    private Object ret = null;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        ArrayList<Method> unCheckMethodList = new ArrayList<>();
        isExecute = true;
        if(method.isAnnotationPresent(Check.class)) {
            Stream.of(method.getAnnotation(Check.class).methodClassName())
                    .forEach(e->{
                        try {
                            Method unCheckMethod = CheckAction.class.getMethod("checkAction",Object.class, Object[].class);
                            boolean temp = (boolean) unCheckMethod.invoke(factory.get(e), target, args);
                            if (!temp) {
                                isExecute = false;
                                unCheckMethodList.add(unCheckMethod);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    });
        }
        if (!isExecute) {
            return ((CheckAction)factory.get(method.getAnnotation(Check.class).UnCheckMethodClassName()))
                    .unCheckAction(unCheckMethodList.toArray(new Method[0]), target, args);
        }
        if(method.isAnnotationPresent(Before.class)) {
            Stream.of(method.getAnnotation(Before.class).methodClassName())
                    .forEach(e-> ((BeforeAction)factory.get(e)).beforeAction(target, args));
        }
        ret = null;
        try {
            ret = myInvoke(proxy, method, args);
        }catch (Throwable ex) {
            if(method.isAnnotationPresent(ThrowsException.class)) {
                Stream.of(method.getAnnotation(ThrowsException.class).methodClassName())
                        .forEach(e->((ThrowsExceptionAction)factory.get(e)).throwExceptionAction(target, ex, args));
            }
        }
        if(method.isAnnotationPresent(AfterReturn.class)) {
            Stream.of(method.getAnnotation(AfterReturn.class).methodClassName())
                    .forEach(e->((AfterReturnAction)factory.get(e)).afterReturnAction(ret, target, args));
        }
        return ret;
    }

}
