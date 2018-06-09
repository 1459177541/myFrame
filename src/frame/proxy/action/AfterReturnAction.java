package frame.proxy.action;

public interface AfterReturnAction extends ProxyAction{
	public void afterReturnAction(Object target, Object... args);
}
