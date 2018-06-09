package frame.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import factory.BeanFactory;
import factory.Factory;
import frame.config.FactoryConfig;
import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;
import frame.proxy.action.ThrowsExceptionAction;
import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;
import frame.proxy.annotation.ThrowsException;

/**
 * 
 * 通过工厂配置文件找到代理方法
 * 
 * @author 杨星辰
 *
 * @param <T>
 */
public class ProxyHandlerByFactory<T> extends ProxyHandler<T> {
	private Factory factory;

	public void setFactory(Factory factory) {
		this.factory = factory;
	}
	
	public void setConfig(FactoryConfig config) {
		factory = new BeanFactory(config);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.isAnnotationPresent(Before.class)) {
			new ArrayList<String>(Arrays.asList(method.getAnnotation(Before.class).methodName()))
				.forEach(e->((BeforeAction)factory.get(e)).beforeAction(args, args));
		}
		Object obj = null;
		try {
			obj = method.invoke(target, args);
		}catch (Throwable ex) {
			if(method.isAnnotationPresent(ThrowsException.class)) {
				new ArrayList<String>(Arrays.asList(method.getAnnotation(ThrowsException.class).methodName()))
					.forEach(e->((ThrowsExceptionAction)factory.get(e)).throwExceptionAction(args, ex, args));
			}			
		}
		if(method.isAnnotationPresent(AfterReturn.class)) {
			new ArrayList<String>(Arrays.asList(method.getAnnotation(AfterReturn.class).methodName()))
				.forEach(e->((AfterReturnAction)factory.get(e)).afterReturnAction(args, args));
		}
		return obj;
	}
}
