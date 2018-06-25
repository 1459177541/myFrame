package frame.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public abstract class ProxyHandler<T> implements InvocationHandler{
	protected T target;
	protected ProxyHandler<T> parent;

	public void setTarget(final T target) {
		this.target = target;
	}

	public void setParent(ProxyHandler<T> parent){
		this.parent = parent;
	}

	protected Object myInvoke(Method method, Object[] args){
		Objects.requireNonNull(target);
		if (null==parent){
			try {
				return method.invoke(target, args);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}else{
			return parent.myInvoke(method, args);
		}
		return null;
	}

}
