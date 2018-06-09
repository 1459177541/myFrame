package frame.proxy.action;

public interface ThrowsExceptionAction extends ProxyAction{
	public void throwExceptionAction(Object target, Throwable ex, Object... args);
}
