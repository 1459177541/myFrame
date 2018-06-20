package util.asynchronized;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AsynAbstractExecutor {
	protected static ThreadPoolExecutor threadPool;
	protected static LinkedBlockingQueue<Runnable> workQueue;

	static {
		workQueue = new LinkedBlockingQueue<>();
		threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
				, Runtime.getRuntime().availableProcessors()*8
				, 8
				, TimeUnit.HOURS
				, workQueue);
	}
	
	public abstract void start();

	public abstract ThreadState getState();
	
}
