package frame.proxy.action;

/**
 * 返回回调方法接口
 * @author 杨星辰
 *
 */
public interface AfterReturnAction extends ProxyAction{
	/**
	 * 方法返回后调用
	 * @param target 代理对象
	 * @param args 方法参数
	 */
	public void afterReturnAction(Object target, Object... args);
}
