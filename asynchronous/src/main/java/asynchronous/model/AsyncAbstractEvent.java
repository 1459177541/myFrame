package asynchronous.model;

import asynchronous.executor.AsyncLevel;

import java.util.Date;

/**
 * 执行动作
 * @author 杨星辰
 *
 */
public abstract class AsyncAbstractEvent implements Runnable, Comparable {

	/**
	 * 状态
	 */
	protected volatile ThreadState state = ThreadState.INIT;

	/**
	 * 错误
	 */
	protected Exception ex = null;

	/**
	 * 优先级
	 */
	protected AsyncLevel asyncLevel;

	protected Date startTime;

	protected long waitTime;

	/*
	 * 构造代码块
	 */
	{
		asyncLevel = AsyncLevel.NORMAL;
		startTime = new Date();
	}

    /**
     * 停止当前线程
     */
	public void shutdown(){
	    state = ThreadState.SHUTDOWN;
	    Thread.currentThread().interrupt();
    }

    public boolean isShutdown(){
	    return ThreadState.SHUTDOWN == state;
    }

    /**
     * 钩子方法，执行停止操作
     */
    protected void doShutdown(){
    }

    /**
     * 设置可等待时间，会改变线程优先级至少为WITHIN
     * @param ms 等待时间
     */
	public void setWaitTime(long ms){
	    this.asyncLevel = AsyncLevel.NOW == asyncLevel
                ? AsyncLevel.NOW
                : AsyncLevel.WITHIN;
	    this.waitTime = ms;
    }

    public boolean isOvertime(){
	    return 0 != waitTime && startTime.getTime() + waitTime > new Date().getTime();
    }

	public void setAsyncLevel(AsyncLevel level){
		this.asyncLevel = level;
	}

	public AsyncLevel getAsyncLevel(){
		return asyncLevel;
	}

	@Override
	public int compareTo(Object o) {
		return asyncLevel.getLevel()-((AsyncAbstractEvent)o).getAsyncLevel().getLevel();
	}

	/**
	 * 设置异常
	 * @param ex 出现的异常
	 */
	public void setException(Exception ex) {
		this.ex = ex;
	}

	public Exception getException(){
		return ex;
	}

	public boolean isException(){
		return ex!=null;
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
	 * @return 是否执行完成
	 */
	public boolean isCompleted() {
		return ThreadState.COMPLETE.equals(state);
	}

}
