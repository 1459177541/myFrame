package proxy.factory.impl;

import factory.BeanFactory;
import factory.BeanFactoryHandler;
import proxy.annotation.*;
import proxy.handler.ProxyHandler;
import proxy.handler.ProxyHandlerBuild;

import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * 代理工厂类，产生代理对象
 * @author 杨星辰
 *
 */
public class ProxyFactory implements BeanFactoryHandler {

//	private BeanFactory factory;
	private ProxyHandlerBuild build;

	public ProxyFactory(){}


	public ProxyFactory setHandlerBuild(ProxyHandlerBuild handlerBuild){
		this.build = Objects.requireNonNull(handlerBuild);
		return this;
	}

	public ProxyHandlerBuild getHandlerBuild() {
		return build;
	}

    @Override
    public BeanFactoryHandler setBeanFactory(BeanFactory factory) {
//        this.factory = factory;
        build = new ProxyHandlerBuild().setBeanFactory(factory);
        return this;
    }

    @Override
	@SuppressWarnings("unchecked")
	public <T> Object get(Class<T> clazz, Object obj) {
		T o = (T) Objects.requireNonNull(obj);
		if (!isProxy(clazz)) {
            return o;
        }
		ProxyHandler<T> handler = (ProxyHandler<T>) Objects.requireNonNull(build).build();
		handler.setTarget(o);
		return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
	}

	private boolean isProxy(Class<?> clazz){
	    return Stream.of(clazz.getMethods())
                    .anyMatch(method -> method.isAnnotationPresent(Before.class)
                            || method.isAnnotationPresent(Check.class)
                            || method.isAnnotationPresent(ThrowsException.class)
                            || method.isAnnotationPresent(AfterReturn.class)
                    )
                || clazz.isAnnotationPresent(Async.class);
    }

}
