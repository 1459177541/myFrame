package frame.proxy.action;

public interface BeforeAction extends ProxyAction{
	public void beforeAction(Object target, Object... args);
}
