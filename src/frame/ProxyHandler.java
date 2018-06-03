package frame;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class ProxyHandler<T> implements InvocationHandler {
	
	protected T target;
	void setTarget(final T target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.isAnnotationPresent(Before.class)) {
			before();
		}
		Object obj = method.invoke(target, args);
		if(method.isAnnotationPresent(After.class)) {
			after();
		}
		return obj;
	}
	
	protected abstract Object before(T target);
	protected Object before() {
		return before(target);
	}
	
	protected abstract Object after(T target);
	protected Object after() {
		return after(target);
	}

}
