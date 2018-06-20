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
	
	/**
	 * 得到正在执行的线程数目
	 * @return
	 */
	public static int getExecuteSize() {
		return threadPool.getPoolSize();
	}
	
	/**
	 * 得到等待执行的线程数目
	 * @return
	 */
	public static int getWaitSize() {
		return workQueue.size();
	}
	
	/**
	 * 得到已执行完成的线程数目
	 * @return
	 */
	public static long getCompleteSize() {
		return threadPool.getCompletedTaskCount();
	}
	
}
