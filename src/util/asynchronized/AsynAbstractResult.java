package util.asynchronized;

/**
 * 可返回结果的线程类
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public abstract class AsynAbstractResult<T> implements Runnable{
	
	private T result;
	
	private Exception ex;
	
	private ThreadState state = ThreadState.WAIT;
	
	private Object lock;
	
	public AsynAbstractResult() {
		lock = new Object();
	}
	
	@Override
	public void run() {
		state = ThreadState.RUNNING;
		try {
			result = execute();
			state = ThreadState.COMPLETE;
		} catch (Exception e) {
			state = ThreadState.EXCEPTION;
		}
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	public void setException(Exception ex) {
		this.ex = ex;
	}
	
	public ThreadState getState() {
		return state;
	}
	
	protected abstract T execute();

	public T getResult() throws Exception {
		if (isCompleted()) {
			return result;
		}else if (ThreadState.RUNNING.equals(state)) {
	        throw new IllegalStateException("未完成");
		}else {
			throw ex;
		}
	}
	
    public void await() throws InterruptedException {
        synchronized (lock) {
          while (!isCompleted()) {
            lock.wait();
          }
        }
      }

	public boolean isCompleted() {
		return ThreadState.COMPLETE.equals(state);
	}
	
}
