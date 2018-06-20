package util.asynchronized;

/**
 * 执行动作
 * @author 杨星辰
 *
 */
public abstract class AsynAbstractEvent implements Runnable{

	/**
	 * 状态
	 */
	protected ThreadState state = ThreadState.WAIT;

	/**
	 * 错误
	 */
	protected Exception ex;

	/**
	 * 同步锁
	 */
	protected Object lock;

	/**
	 * 构造代码块，初始化同步锁
	 */
	{
		lock = new Object();
	}

	/**
	 * 设置异常
	 * @param ex
	 */
	public void setException(Exception ex) {
		this.ex = ex;
	}

	/**
	 * 得到当前状态
	 * @return 当前状态
	 */
	public ThreadState getState() {
		return state;
	}

	/**
	 * 是否执行完成
	 * @return 
	 */
	public boolean isCompleted() {
		return ThreadState.COMPLETE.equals(state);
	}

	/**
	 * 等待
	 * @throws InterruptedException
	 */
	public void await() throws InterruptedException {
		synchronized (lock) {
			while (!isCompleted()) {
				lock.wait();
			}
		}
	}

}
