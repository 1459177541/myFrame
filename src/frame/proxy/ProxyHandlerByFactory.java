package frame.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import factory.BeanFactory;
import factory.Factory;
import frame.config.FactoryConfig;
import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;
import frame.proxy.action.CheckAction;
import frame.proxy.action.ThrowsExceptionAction;
import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;
import frame.proxy.annotation.Check;
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
	
	
	private boolean isExecute = true;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ArrayList<Method> unCheckMethodList = new ArrayList<>();
		isExecute = true;
		if(method.isAnnotationPresent(Check.class)) {
			new ArrayList<String>(Arrays.asList(method.getAnnotation(Check.class).methodClassName()))
			.forEach(e->{
				try {
					Method unCheckMethod = CheckAction.class.getMethod("checkAction",Object.class, Object[].class);	//BUG!
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
					.unCheckAction(unCheckMethodList.toArray(new Method[unCheckMethodList.size()]), target, args);
		}
		if(method.isAnnotationPresent(Before.class)) {
			new ArrayList<String>(Arrays.asList(method.getAnnotation(Before.class).methodClassName()))
				.forEach(e->((BeforeAction)factory.get(e)).beforeAction(target, args));
		}
		Object obj = null;
		try {
			obj = method.invoke(target, args);
		}catch (Throwable ex) {
			if(method.isAnnotationPresent(ThrowsException.class)) {
				new ArrayList<String>(Arrays.asList(method.getAnnotation(ThrowsException.class).methodClassName()))
					.forEach(e->((ThrowsExceptionAction)factory.get(e)).throwExceptionAction(target, ex, args));
			}			
		}
		if(method.isAnnotationPresent(AfterReturn.class)) {
			new ArrayList<String>(Arrays.asList(method.getAnnotation(AfterReturn.class).methodClassName()))
				.forEach(e->((AfterReturnAction)factory.get(e)).afterReturnAction(target, args));
		}
		return obj;
	}
}
