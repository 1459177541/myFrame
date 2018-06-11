package frame.proxy.action;

/**
 * 方法执行前调用接口
 * @author 杨星辰
 *
 */
public interface BeforeAction extends ProxyAction{
	/**
	 * 方法调用前执行方法
	 * @param target 代理对象
	 * @param args 方法参数
	 */
	public void beforeAction(Object target, Object... args);
}
