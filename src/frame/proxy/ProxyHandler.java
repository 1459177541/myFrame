package frame.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ProxyHandler<T> implements InvocationHandler {
	
	private T target;
	private ArrayList<BeforeAction> beforeAction;
	private ArrayList<AfterReturnAction> afterReturnAction;
	
	public ProxyHandler() {
		beforeAction = new ArrayList<>();
		afterReturnAction = new ArrayList<>();
	}
	
	public ProxyHandler(T target) {
		this();
		this.target = target;
	}
	
	void setTarget(final T target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.isAnnotationPresent(Before.class)) {
			beforeAction.forEach(e->e.beforeAction(args));
		}
		Object obj = method.invoke(target, args);
		if(method.isAnnotationPresent(AfterReturn.class)) {
			afterReturnAction.forEach(e->e.afterReturnAction(args));
		}
		return obj;
	}
	
}
