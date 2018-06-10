package frame.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;

import frame.proxy.action.AfterReturnAction;
import frame.proxy.action.BeforeAction;
import frame.proxy.action.ThrowsExceptionAction;
import frame.proxy.annotation.AfterReturn;
import frame.proxy.annotation.Before;
import frame.proxy.annotation.ThrowsException;

/**
 * 
 * 通过注解上标识类全名找到代理方法
 * 
 * @author 杨星辰
 *
 * @param <T>
 */
public class ProxyHandlerByClassName<T> extends ProxyHandler<T> {
	
	protected ArrayList<BeforeAction> beforeAction;
	protected ArrayList<AfterReturnAction> afterReturnAction;
	protected ArrayList<ThrowsExceptionAction> throwsExceptionAction;
	
	public ProxyHandlerByClassName() {
		beforeAction = new ArrayList<>();
		afterReturnAction = new ArrayList<>();
	}
	
	public ProxyHandlerByClassName(T target) {
		this();
		this.target = target;
	}
	
	public void setBeforeAction(ArrayList<BeforeAction> beforeAction) {
		this.beforeAction = beforeAction;
	}

	public void setAfterReturnAction(ArrayList<AfterReturnAction> afterReturnAction) {
		this.afterReturnAction = afterReturnAction;
	}
	
	public void setThrowsExceptionAction(ArrayList<ThrowsExceptionAction> throwsExceptionAction) {
		this.throwsExceptionAction = throwsExceptionAction;
	}

	public void addBeforeAction(BeforeAction beforeAction) {
		if (null==beforeAction) {
			this.beforeAction = new ArrayList<>();
		}
		this.beforeAction.add(beforeAction);
	}
	
	public void addAfterReturnAction(AfterReturnAction afterReturnAction) {
		if (null==afterReturnAction) {
			this.afterReturnAction = new ArrayList<>();
		}
		this.afterReturnAction.add(afterReturnAction);
	}
	
	public void addThrowsExceptionAction(ThrowsExceptionAction throwsExceptionAction) {
		if (null==throwsExceptionAction) {
			this.throwsExceptionAction = new ArrayList<>();
		}
		this.throwsExceptionAction.add(throwsExceptionAction);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.isAnnotationPresent(Before.class)) {
			beforeAction.forEach(e->{
				String[] methodNames = method.getAnnotation(Before.class).methodClassName();
				for (String methodName : methodNames) {
					if (methodName.equals(e.getClass().getName())) {
						e.beforeAction(target, args);
					}
				}
			});
		}
		Object obj = null;
		try {
			obj = method.invoke(target, args);
		}catch (Throwable ex) {
			if(method.isAnnotationPresent(ThrowsException.class)) {
				throwsExceptionAction.forEach(e->{
					String[] methodNames = method.getAnnotation(ThrowsException.class).methodClassName();
					for (String methodName : methodNames) {
						if (methodName.equals(e.getClass().getName())) {
							e.throwExceptionAction(target, ex, args);
						}
					}
				});
			}			
		}
		if(method.isAnnotationPresent(AfterReturn.class)) {
			afterReturnAction.forEach(e->{
				String[] methodNames = method.getAnnotation(AfterReturn.class).methodClassName();
				for (String methodName : methodNames) {
					if (methodName.equals(e.getClass().getName())) {
						e.afterReturnAction(target, args);
					}
				}
			});
		}
		return obj;
	}
	
	
}
