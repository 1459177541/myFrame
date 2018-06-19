package util.asynchronized;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsynExecutor<T>{

	private AsynEvent<T> event;
	private static ThreadPoolExecutor threadPool;
	private static LinkedBlockingQueue<Runnable> workQueue;

	static {
		workQueue = new LinkedBlockingQueue<>();
		threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
				, Runtime.getRuntime().availableProcessors()*8
				, 8
				, TimeUnit.HOURS
				, workQueue);
	}

	public AsynExecutor(AsynEvent<T> event) {
		this.event = event;
	}

	public AsynExecutor() {
		this.event = null;
	}	
	
	public void start() {
		try {
			threadPool.execute(Objects.requireNonNull(event));
		} catch (Exception e) {
			event.setException(e);
		}
	}
	
	public void start(AsynEvent<T> event) {
		this.event = event;
		try {
			threadPool.execute(event);
		} catch (Exception e) {
			event.setException(e);
		}
	}

	public T getResult() throws Exception{
		if (!event.isCompleted()) {
			event.await();
		}
		return event.getResult();
	}

	public ThreadState getState() {
		return event.getState();
	}

}
