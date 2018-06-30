package util.asynchronized;

/**
 * 可返回结果的线程类
 * @author 杨星辰
 *
 * @param <T> 返回值
 */
public abstract class AsyncResult<T> extends AsyncAbstractEvent {
	
	private T result;
	
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
	
	public T waitAndGetResult() {
		if(!isCompleted()) {
			try {
				await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	


}
