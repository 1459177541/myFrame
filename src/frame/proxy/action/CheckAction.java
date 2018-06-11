package frame.proxy.action;

/**
 * 检查动作接口
 * @author 杨星辰
 *
 */
public interface CheckAction extends ProxyAction{
	/**
	 * 检查是否执行方法
	 * @param target 代理对象
	 * @param args 方法参数
	 * @return 是否执行
	 */
	public boolean checkAction(Object target, Object...args);
}
