package util.asynchronized;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AsyncAbstractExecutor {
	protected static ThreadPoolExecutor threadPool;
	protected static PriorityBlockingQueue<Runnable> workQueue;

	static {
		workQueue = new PriorityBlockingQueue<>();
		threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2
				, Runtime.getRuntime().availableProcessors()*8
				, 8
				, TimeUnit.HOURS
				, workQueue);
	}

	protected AsyncLevel level = AsyncLevel.NORMAL;

	public void setLevel(AsyncLevel level){
		this.level = level;
	}
	
	public abstract void start();

	public abstract ThreadState getState();

	protected static void execute(AsyncLevel level, AsyncAbstractEvent event){
	    try {
            if (AsyncLevel.IMMEDIATELY.equals(level) && getWaitSize() > 0) {
                new Thread(event).start();
            } else {
                threadPool.execute(event);
            }
        }catch (Exception e){
	        event.setException(e);
        }
	}

	protected static void execute(AsyncAbstractEvent event){
	    execute(event.getAsyncLevel(),event);
    }

	/**
	 * 得到等待执行的线程数目
	 * @return 等待执行的线程数目
	 */
	public static int getWaitSize() {
		return workQueue.size();
	}
	
	/**
	 * 得到已执行完成的线程数目
	 * @return 已执行完成的线程数目
	 */
	public static long getCompleteSize() {
		return threadPool.getCompletedTaskCount();
	}
	
}
