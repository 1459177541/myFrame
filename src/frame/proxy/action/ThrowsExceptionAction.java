package frame.proxy.action;

/**
 * 抛出异常处理接口
 * @author 杨星辰
 *
 */
public interface ThrowsExceptionAction extends ProxyAction{
	/**
	 * 抛出异常调用方法
	 * @param target 代理对象
	 * @param ex 抛出的异常
	 * @param args 方法参数
	 */
	public void throwExceptionAction(Object target, Throwable ex, Object... args);
}
