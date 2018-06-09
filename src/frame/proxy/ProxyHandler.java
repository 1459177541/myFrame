package frame.proxy;

import java.lang.reflect.InvocationHandler;

public abstract class ProxyHandler<T> implements InvocationHandler{
	protected T target;

	public void setTarget(final T target) {
		this.target = target;
	}
}
