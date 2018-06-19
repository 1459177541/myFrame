package util.asynchronized;

public abstract class AsynEvent<T> implements Runnable{
	
	private T result;
	
	private Exception ex;
	
	private ThreadState state = ThreadState.WAIT;
	
	private Object lock;
	
	public AsynEvent() {
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
